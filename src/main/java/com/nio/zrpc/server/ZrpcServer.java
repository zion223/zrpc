package com.nio.zrpc.server;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.I0Itec.zkclient.ZkClient;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nio.zrpc.registry.consul.ConsulUtil;
import com.nio.zrpc.registry.consul.entity.ServiceRegisterDefinition;
import com.nio.zrpc.registry.zookeeper.ZkConstant;
import com.nio.zrpc.registry.zookeeper.ZookeeperUtil;
import com.nio.zrpc.registry.zookeeper.entity.ZkServiceRegisterDefinition;
import com.nio.zrpc.tag.definition.RegistryDefinition;

public class ZrpcServer {
	private static final Logger log = LoggerFactory.getLogger(ZrpcServer.class);

	private static volatile String registryAddress;
	/**
	 * flag=true 为consul注册 
	 * flag=false 为zookeeper注册
	 */
	public static volatile  boolean registryFlag;
	
	//consul的注册信息
	public static List<ServiceRegisterDefinition> ConserviceList=new ArrayList<ServiceRegisterDefinition>();
	//zookeeper的注册信息
	public static List<ZkServiceRegisterDefinition> ZkserviceList=new ArrayList<ZkServiceRegisterDefinition>();
	
	public static void StartServer(String add,String path) {
		//add  "127.0.0.1:8080"
		SocketAddress address =new InetSocketAddress(add.split(":")[0], Integer.parseInt(add.split(":")[1]));
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		
		ExecutorService boss = Executors.newCachedThreadPool();
		ExecutorService worker = Executors.newCachedThreadPool();
		//设置NioSocket工厂
		serverBootstrap.setFactory(new NioServerSocketChannelFactory(boss,worker));
		//设置管道工厂
		serverBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			
			public ChannelPipeline getPipeline() throws Exception {
				//常见管道
				ChannelPipeline pipeline = Channels.pipeline();
				
				pipeline.addLast("decoder", new StringDecoder());
				pipeline.addLast("encoder", new StringEncoder());
				pipeline.addLast("ServerHandler", new ServerHandler());
				
				return pipeline;
			}
		});
		serverBootstrap.bind(address);
		log.info("Server started!!!!!!!!!!!");
		//获取Spring容器
		SpringInit(path);
		
	}
	public static void main(String[] args) {
		String msg="127.0.0.1:8080";
		String[] split = msg.split(":");
		System.out.println(split[0]+":"+split[1]);
	}
	private static void SpringInit(String path){
		 @SuppressWarnings("resource")
		ApplicationContext ac = new ClassPathXmlApplicationContext(path);
		 RegistryDefinition registryDef = (RegistryDefinition) ac.getBean("registry");
		 log.info("注册中心为:"+registryDef.getName());
		 registryAddress = registryDef.getAddress();
		 //判断是consul还是zookeeper
		 if(registryDef.getName().equals("Consul")){
			 registryFlag=true;
			 ConsulRegister();
		 }else if(registryDef.getName().equals("Zookeeper")){
			 registryFlag=false;
			 //zk注册
			 ZookeeperRegister();
		 }else{
			 throw new IllegalArgumentException("没有这个注册中心呦");
		 }
		  
	}
	private static void ZookeeperRegister() {
		ZkClient zkClient = ZookeeperUtil.getInstance();
		//ZkClient zkClient = new ZkClient(registryAddress);
		zkClient.createPersistent(ZkConstant.ROOT_TAG);
		
		for(ZkServiceRegisterDefinition zsl:ZkserviceList){
			log.info("============="+zsl.getInterfaceName()+":"+zsl.getRef()+"=============");
			zkClient.createPersistent(ZkConstant.ROOT_TAG+"/"+zsl.getInterfaceName(),zsl.getRef());
		}
		//zkClient.close();
		
	}
	private static void ConsulRegister() {
		
		  ConsulUtil consulUtil = new ConsulUtil(registryAddress);
		  //注册服务到Consul上
		  // id name tag address port
	
		  for(ServiceRegisterDefinition srd:ConserviceList){
			  
			  consulUtil.serviceRegister(srd);
		  }
	}
	public static String getRegistryAddress(){
		return registryAddress;
	}
}
