package com.nio.zrpc.client;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.nio.consumer.controller.HelloServiceController;
import com.nio.zrpc.definition.RpcDefinition;
import com.nio.zrpc.hystrix.FallBackDefinition;
import com.nio.zrpc.hystrix.anno.Command;
import com.nio.zrpc.serialization.KryoUtil;
import com.nio.zrpc.tag.definition.ReferenceDefinition;
import com.nio.zrpc.util.ClassUtil;

public class ZrpcClient {
	private static final Logger log = LoggerFactory.getLogger(ZrpcClient.class);
	private List<String>  packageName = new ArrayList<String>();
	private static HashMap<String, Object> fallbackMap = new HashMap<String, Object>();
	private static volatile ApplicationContext ac;
	private static Channel channel;
	// protected static volatile Object result;
	
	// protected static Object lock = new Object();
	volatile static Class returnType;

	public ZrpcClient() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		// 维护一个controller集合
		// 判断方法上是否有command注解
		
		packageName=ClassUtil.searchClass("com.nio.consumer");
		
		
		filterAndInstance();
	}

	

	@SuppressWarnings({ "unused", "unchecked" })
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
				for(Method m:methods){
					//判断方法上有无command注解
					if(m.isAnnotationPresent(Command.class)){
						//需要调用fall方法的类 className
						Command command = m.getAnnotation(Command.class);
						String fallbackMethod = command.fallbackMethod();
						String fallbackType = command.fallbackType();
						//com.nio.service.helloService:fall
						
						fallbackMap.put(fallbackType, new FallBackDefinition(className, fallbackMethod));
					}
					
					
				}
				
			}  else {
				continue;
			}
		}
	}

	public  void ZrpcClient(String add,String path ) {
		SocketAddress address =new InetSocketAddress(add.split(":")[0], Integer.parseInt(add.split(":")[1]));

		ClientBootstrap clientBootstrap = new ClientBootstrap();
		ExecutorService boss = Executors.newCachedThreadPool();
		ExecutorService worker = Executors.newCachedThreadPool();
		clientBootstrap.setFactory(new NioClientSocketChannelFactory(boss,
				worker));
		clientBootstrap.setPipelineFactory(new ChannelPipelineFactory() {

			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addLast("decoder", new StringDecoder());
				pipeline.addLast("encoder", new StringEncoder());
				pipeline.addLast("hiHandler", new ClientHandler());
				return pipeline;
			}
		});
		ChannelFuture connect = clientBootstrap.connect(address);
		channel = connect.getChannel();
		log.info("client start");
		SpringInit(path);
	}

	private void SpringInit(String path) {
		if(path.equals("")){
			throw new NullPointerException("配置文件呢??");
		}
		ac = new ClassPathXmlApplicationContext(path);
		
	}

	public static Object getBean(String serviceId) throws ClassNotFoundException{
		
		ReferenceDefinition rdf = (ReferenceDefinition) ac.getBean(serviceId);
		if(rdf==null&&rdf.equals("")){
			throw new NullPointerException("配置文件中没有配置服务名");
		}
		rdf.getStrategy();
		String interfaceName = rdf.getInterfaceName();
		Class forName =  Class.forName(interfaceName);
		return refer(forName);
	
	}

	@SuppressWarnings("unchecked")
	public static Object refer(final Class<?> interfaceName) {
		return  Proxy.newProxyInstance(ZrpcClient.class.getClassLoader(),
				new Class[] { interfaceName }, new InvocationHandler() {

					public Object invoke(Object proxy, Method method,
							Object[] arguments) throws Throwable {
						// 转发请求 交给服务端执行
						// interface com.nio.service.HelloService
						//com.nio.service.HelloService
						String infName = interfaceName.toString()
								.substring(10);
						returnType = method.getReturnType();
						//发送序列化的数据 加入FallBack机制
						//只加入fallback方法
						FallBackDefinition fallback=null;
						for (Entry<String, Object> entry :fallbackMap.entrySet()) {
							
							if(infName.equals(entry.getKey().toString())){
								//需要使用Hystrix
								 fallback = (FallBackDefinition) entry.getValue();
							}
							
						}
						
						
						RpcDefinition rpc = new RpcDefinition(infName,
								method.getName(), method.getParameterTypes(),
								arguments,fallback);
						// TODO 加入其他序列化协议支持(kryo)
						String jsonString = JSONObject.toJSONString(rpc);
						
						String kryoString = KryoUtil.serializationObject(rpc);
						ChannelFuture future = channel.write(jsonString);
						future.addListener(new ChannelFutureListener() {

							public void operationComplete(ChannelFuture future)
									throws Exception {
								// 消息发送完毕
								log.info("消息发送完毕");

							}

						});
						getResultThread getResultThread = new getResultThread();
						getResultThread.start();
						Thread.sleep(1000);
						return getResultThread.result;
					}
				});

	}

}

class getResultThread extends Thread {
	private static final Logger log = LoggerFactory.getLogger(getResultThread.class);
	protected static volatile Object result;

	protected static Object lock = new Object();

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		while (result == null) {
			synchronized (lock) {
				try {
					lock.wait();
					log.info("得到result:" + result+result.toString()+result.getClass());
					
					if (ZrpcClient.returnType == String.class) {
						
						return;
					} else if(result.getClass()==ZrpcClient.returnType){
						return ;
					}else{
						//class com.nio.entity.User
						log.info("返回类型:" + ZrpcClient.returnType);
					
							Object parseObject = JSONObject.parseObject(
									result.toString(), ZrpcClient.returnType);

							log.info(parseObject.toString());
							result = parseObject;
							log.info("get Thread:"
									+ Thread.currentThread().getName());
						return;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

}

