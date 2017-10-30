package com.nio.zrpc.server;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Server {

	public static void ZrpcServer(InetSocketAddress add,String path) {
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
		serverBootstrap.bind(add);
		System.out.println("ZrpcServer started!!!!!!!!!!!");
		springinit(path);
		
	}
	private static void springinit(String path){
		 ApplicationContext ac = new ClassPathXmlApplicationContext(path);
		 
	}
}
