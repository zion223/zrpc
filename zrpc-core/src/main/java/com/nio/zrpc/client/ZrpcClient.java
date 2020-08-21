package com.nio.zrpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;

import com.nio.zrpc.coder.RpcDecoder;
import com.nio.zrpc.coder.RpcEncoder;
import com.nio.zrpc.definition.RpcRequest;
import com.nio.zrpc.definition.RpcResponse;
import com.nio.zrpc.hystrix.FallBackDefinition;
import com.nio.zrpc.hystrix.anno.Command;
import com.nio.zrpc.registry.strategy.FindLoadBalanceStrategy;
import com.nio.zrpc.registry.zookeeper.ZkConstant;
import com.nio.zrpc.registry.zookeeper.ZookeeperUtil;
import com.nio.zrpc.tag.definition.ReferenceDefinition;
import com.nio.zrpc.tag.definition.RegistryDefinition;
import com.nio.zrpc.util.ClassUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ZrpcClient implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(ZrpcClient.class);
    private List<String> packageName = new ArrayList<String>();
    private static HashMap<String, Object> fallbackMap = new HashMap<String, Object>();
    private static volatile ApplicationContext ac;
    private static volatile String registryAddress;

    public ZrpcClient() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        // 维护一个controller集合
        // 判断方法上是否有command注解

        //TODO 扫描类的路径
        packageName = ClassUtil.searchClass("com.nio.consumer");

        filterAndInstance();
    }

    @SuppressWarnings("unchecked")
    private void filterAndInstance() throws ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        if (packageName.size() <= 0) {
            return;
        }
        for (String className : packageName) {
            @SuppressWarnings("rawtypes")
            Class clazz = Class.forName(className);
            //判断是Controller注解 并且方法上有Command注解
            if (clazz.isAnnotationPresent(Controller.class)) {
                Method[] methods = clazz.getDeclaredMethods();
                //判断方法上有无Command注解
                //key:fallbackType value:fallbackmethod
                for (Method m : methods) {
                    //判断方法上有无command注解
                    if (m.isAnnotationPresent(Command.class)) {
                        //需要调用fall方法的类 className
                        Command command = m.getAnnotation(Command.class);
                        String fallbackMethod = command.fallbackMethod();
                        String fallbackType = command.fallbackType();
                        //com.nio.service.helloService:fall

                        fallbackMap.put(fallbackType, new FallBackDefinition(className, fallbackMethod));
                    }
                }
            } else {
                continue;
            }
        }
    }

    public void StartClient(String path) throws InterruptedException {
        //需要向注册中心订阅RPC服务,根据返回的服务列表，与具体的Server建立连接进行调用
        initSpring(path);
        log.info("client start");
    }

    private static void invokeNettyRequest(String address, RpcRequest request) throws InterruptedException {

        //add = "192.168.100.3:8000"
        String host = StringUtils.substringBefore(address, ":");
        int port = Integer.parseInt(StringUtils.substringAfter(address, ":"));

        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new RpcEncoder(RpcRequest.class)) // 将 RPC 请求进行编码（为了发送请求）
                                .addLast(new RpcDecoder(RpcResponse.class)) // 将 RPC 响应进行解码（为了处理响应）
                                .addLast(new ClientHandler()); // 使用 RpcClient 发送 RPC 请求
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true);

        ChannelFuture future = bootstrap.connect(host, port).sync();
        Channel channel = future.channel();
        channel.writeAndFlush(request).sync().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                log.info("消息发送完毕");
            }
        });

        future.channel().closeFuture().sync();
    }

    private void initSpring(String path) {
        if (path.equals("")) {
            throw new NullPointerException("配置文件呢??");
        }
        ac = new ClassPathXmlApplicationContext(path);
        RegistryDefinition registryDef = (RegistryDefinition) ac.getBean("registry");
        setRegistryAddress(registryDef.getAddress());
    }

    public Object getBean(String serviceId) throws ClassNotFoundException {

        ReferenceDefinition rdf = (ReferenceDefinition) ac.getBean(serviceId);
        if (rdf == null) {
            throw new NullPointerException("配置文件中没有配置服务名");
        }
        String strategy = rdf.getStrategy();
        String interfaceName = rdf.getInterfaceName();
        Class<?> forName = Class.forName(interfaceName);
        return refer(forName, strategy);
    }

    public static Object refer(final Class<?> interfaceName, final String strategy) {
        return Proxy.newProxyInstance(ZrpcClient.class.getClassLoader(),
                new Class[]{interfaceName}, new InvocationHandler() {

                    @SuppressWarnings("static-access")
                    public Object invoke(Object proxy, Method method,
                                         Object[] arguments) throws Throwable {

                        String infName = interfaceName.toString().substring(10);
                        //需要向注册中心订阅RPC服务,根据返回的服务列表，与具体的Server建立连接进行调用
                        ZkClient zkClient = ZookeeperUtil.getInstance();
                        try {
                            zkClient.createPersistent(ZkConstant.ROOT_TAG);
                        } catch (Exception e) {
                            log.info("Zookeeper中已经存在 " + ZkConstant.ROOT_TAG + "节点");
                        }
                        // 创建消费者节点

                        //zkClient.createPersistent(ZkConstant.ROOT_TAG + "/"+ infName + ZkConstant.CONSUMER_TAG + "/"+InetAddress.getLocalHost().getHostAddress().toString());
                        List<String> ServiceAddress = zkClient.getChildren(ZkConstant.ROOT_TAG + "/"
                                + infName + ZkConstant.PROVIDER_TAG);

                        String Serviceinstance = FindLoadBalanceStrategy.getStrategyByName(strategy).getInstance(ServiceAddress);
                        //returnType = method.getReturnType();
                        //发送序列化的数据 加入FallBack机制
                        //只加入fallback方法
                        log.info("服务调用的地址:" + Serviceinstance);
                        FallBackDefinition fallback = null;
                        for (Entry<String, Object> entry : fallbackMap.entrySet()) {

                            if (infName.equals(entry.getKey().toString())) {
                                //需要使用Hystrix
                                fallback = (FallBackDefinition) entry.getValue();
                            }
                        }
                        RpcRequest request = new RpcRequest(infName,
                                method.getName(), method.getParameterTypes(),
                                arguments, fallback);

                        invokeNettyRequest(Serviceinstance, request);

                        getResultThread getResultThread = new getResultThread();
                        getResultThread.start();
                        Thread.sleep(1000);
                        return getResultThread.result;
                    }
                });

    }

    public static String getRegistryAddress() {
        return registryAddress;
    }

    public static void setRegistryAddress(String registryAddress) {
        ZrpcClient.registryAddress = registryAddress;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

}

class getResultThread extends Thread {
    private static final Logger log = LoggerFactory.getLogger(getResultThread.class);
    protected volatile static Object result;

    protected static Object lock = new Object();

    @Override
    public void run() {
        while (result == null) {
            synchronized (lock) {
                try {
                    lock.wait();
                    log.info("得到服务返回结果:" + result);
                    return;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}

