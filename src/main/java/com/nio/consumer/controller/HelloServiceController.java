package com.nio.consumer.controller;

import java.net.InetSocketAddress;

import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.nio.entity.User;
import com.nio.service.HelloService;
import com.nio.zrpc.client.Client;
import com.nio.zrpc.hystrix.anno.Command;

@Service
@Controller
public class HelloServiceController {
	
	@Test
	@Command(fallbackType="com.nio.service.HelloService")
	public void helloService() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		Client client = new Client();
//		client.ZrpcClient(new InetSocketAddress("127.0.0.1",8000));
//		HelloService service = Client.refer(HelloService.class);
//		//String result = service.sayHello("Zrp");
//		//System.out.println("result:"+result);

//		User createUser = service.createUser("zhangrp", "12");
//		System.out.println("返回的user:"+createUser);
	}
	
	public User fall(){
		User user = new User();
		user.setAge(0);
		user.setName("0");
		return user;
	}
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Client client = new Client();
		client.ZrpcClient(new InetSocketAddress("127.0.0.1",8000));
		HelloService service = Client.refer(HelloService.class);
		//String result = service.sayHello("Zrp");
		//System.out.println("result:"+result);

		User createUser = service.createUser("zhangrp",12);
		System.out.println("返回的user:"+createUser);
	}
}
