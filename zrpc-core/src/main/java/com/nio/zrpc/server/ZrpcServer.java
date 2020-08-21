package com.nio.zrpc.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nio.zrpc.coder.RpcDecoder;
import com.nio.zrpc.coder.RpcEncoder;
import com.nio.zrpc.definition.RpcRequest;
import com.nio.zrpc.definition.RpcResponse;
import com.nio.zrpc.registry.consul.ConsulUtil;
import com.nio.zrpc.registry.consul.entity.ServiceRegisterDefinition;
import com.nio.zrpc.registry.zookeeper.ZkConstant;
import com.nio.zrpc.registry.zookeeper.ZookeeperUtil;
import com.nio.zrpc.registry.zookeeper.entity.ZkServiceRegisterDefinition;
import com.nio.zrpc.tag.definition.RegistryDefinition;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ZrpcServer implements InitializingBean {
	private static final Logger log = LoggerFactory.getLogger(ZrpcServer.class);

	private static volatile String registryAddress;
	/**
	 * flag=true 为consul注册 flag=false 为zookeeper注册
	 */
	public static volatile boolean registryFlag;
	public static ApplicationContext ac;

	private static volatile int port;


	// consul的注册信息
	public static List<ServiceRegisterDefinition> ConserviceList = new ArrayList<ServiceRegisterDefinition>();
	// zookeeper的注册信息
	public static List<ZkServiceRegisterDefinition> ZkserviceList = new ArrayList<ZkServiceRegisterDefinition>();

	public static void StartServer(String path) throws InterruptedException, UnknownHostException {
		SpringInit(path);
		String host = InetAddress.getLocalHost().getHostAddress().toString();


		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel channel) throws Exception {
							channel.pipeline()
									.addLast(new RpcDecoder(RpcRequest.class)) // 将 RPC 请求进行解码（为了处理请求）
									.addLast(new RpcEncoder(RpcResponse.class)) // 将 RPC 响应进行编码（为了返回响应）
									.addLast(new ServerHandler()); // 处理 RPC 请求
						}
					})
					.option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture future = bootstrap.bind(host, port).sync();
			log.info("Server start on port {} " + port);
			// 获取Spring容器

			future.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	/**
	 * Spring容器初始化
	 *
	 * @param path
	 * @throws UnknownHostException
	 */
	private static void SpringInit(String path) throws UnknownHostException {

		ac = new ClassPathXmlApplicationContext(path);
		RegistryDefinition registryDef = (RegistryDefinition) ac.getBean("registry");
		log.info("注册中心为:" + registryDef.getName());
		registryAddress = registryDef.getAddress();
		// 判断是consul还是zookeeper
		if (registryDef.getName().equals("Consul")) {
			registryFlag = true;
			ConsulRegister();
		} else if (registryDef.getName().equals("Zookeeper")) {
			registryFlag = false;
			// zk注册
			ZookeeperRegister();
		} else {
			throw new IllegalArgumentException("没有这个注册中心呦");
		}

	}

	/**
	 * 在Zookeeper中注册服务
	 *
	 * @throws UnknownHostException
	 */
	private static void ZookeeperRegister() throws UnknownHostException {

		ZkClient zkClient = ZookeeperUtil.getInstance();

		try {
			zkClient.createPersistent(ZkConstant.ROOT_TAG);
		} catch (Exception e) {
			log.info("Zookeeper中已经存在 " + ZkConstant.ROOT_TAG + "节点");
		}

		for (ZkServiceRegisterDefinition zsl : ZkserviceList) {
			port = Integer.parseInt(zsl.getPort());
			log.info("在Zookeeper中注册的接口名称" + zsl.getInterfaceName() + "===" + "服务的地址:" + InetAddress.getLocalHost().getHostAddress() + ":" + zsl.getPort());

			try {
				zkClient.createPersistent(ZkConstant.ROOT_TAG + "/" + zsl.getInterfaceName(), "");
				zkClient.createPersistent(ZkConstant.ROOT_TAG + "/" + zsl.getInterfaceName() + ZkConstant.PROVIDER_TAG, "");
			} catch (Exception e) {
				log.info("Zookeeper中已经存在 " + ZkConstant.ROOT_TAG + "/" + zsl.getInterfaceName() + "节点");
			}
			zkClient.createPersistent(ZkConstant.ROOT_TAG + "/" + zsl.getInterfaceName() + ZkConstant.PROVIDER_TAG + "/" + InetAddress.getLocalHost().getHostAddress() + ":" + zsl.getPort());
		}

	}

	/**
	 * 在Consul中注册服务
	 */
	private static void ConsulRegister() {

		ConsulUtil consulUtil = new ConsulUtil(registryAddress);
		// 注册服务到Consul上
		// id name tag address port

		for (ServiceRegisterDefinition srd : ConserviceList) {

			consulUtil.RegisterService(srd);
		}
	}

	public static String getRegistryAddress() {
		return registryAddress;
	}

	public static ApplicationContext getApplicationContext() {
		if (ac == null) {
			throw new NullPointerException("Spring容器未初始化成功");
		}
		return ac;
	}

	@Override
	public void afterPropertiesSet() throws Exception {


	}
}
