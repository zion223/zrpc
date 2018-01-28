package com.nio.provider;

import java.net.UnknownHostException;

import com.nio.zrpc.server.ZrpcServer;


public class TestServer {

	public static void main(String[] args) throws InterruptedException, UnknownHostException {
		  //创建服务器    地址和xml文件的路径
		  ZrpcServer.StartServer("com/nio/provider/provider.xml");
		  //在Consul注册中心中注册服务
		  
		  
	}
}
