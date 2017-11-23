package com.nio.provider;

import com.nio.zrpc.server.ZrpcServer;


public class TestServer {

	public static void main(String[] args) {
		  //创建服务器    地址和xml文件的路径
		  ZrpcServer.StartServer("127.0.0.1:8000","com/nio/provider/provider.xml");
		  //在Consul注册中心中注册服务
		  
		  
	}
}
