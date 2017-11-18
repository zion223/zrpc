package com.nio.consumer.test;

import java.net.InetSocketAddress;

import com.nio.entity.User;
import com.nio.service.HelloService;
import com.nio.zrpc.client.Client;


public class testClient {

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException {
	

		//发送序列化好的对象
		Client client = new Client();
		client.ZrpcClient(new InetSocketAddress("127.0.0.1",8000));
		HelloService service = Client.refer(HelloService.class);
		//String result = service.sayHello("Zrp");
		//System.out.println("result:"+result);
		
		User createUser = service.createUser("zhangrp", 12);
		System.out.println("返回的user:"+createUser);
		
		//String resultHi = service.sayHi("ZRP");
		//System.out.println(createUser);
		
	}
	
	
}
