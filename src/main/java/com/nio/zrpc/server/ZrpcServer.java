package com.nio.zrpc.server;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

import com.nio.zrpc.consul.ConsulUtil;
import com.nio.zrpc.consul.entity.ServiceRegisterDefinition;
import com.nio.zrpc.consul.request.ServiceRequest;
import com.nio.zrpc.tag.definition.RegistryDefinition;

public class ZrpcServer {
	private static final Logger log = LoggerFactory.getLogger(ZrpcServer.class);

	private static volatile String registryAddress;
	public static List<ServiceRegisterDefinition> serviceList=new ArrayList<ServiceRegisterDefinition>();
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
		 ApplicationContext ac = new ClassPathXmlApplicationContext(path);
		 RegistryDefinition registryDef = (RegistryDefinition) ac.getBean("registry");
		 
		  registryAddress = registryDef.getAddress();
		  ConsulUtil consulUtil = new ConsulUtil(registryAddress);
		  //注册服务到Consul上
		  // id name tag address port
	
		  for(ServiceRegisterDefinition srd:serviceList){
			  
			  List<Integer> port = srd.getPort();
			  
			  consulUtil.serviceRegister(srd);
		  }
		  //consulUtil.serviceRegister();
		  
	}
	public static String getRegistryAddress(){
		return registryAddress;
	}
}
