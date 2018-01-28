package com.nio.consumer.controller;

import java.net.InetSocketAddress;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.nio.entity.User;
import com.nio.service.HelloService;
import com.nio.zrpc.client.ZrpcClient;
import com.nio.zrpc.hystrix.anno.Command;
import com.nio.zrpc.registry.consul.ConsulUtil;

@Service
@Controller
public class HelloServiceController {
	private static final Logger log = LoggerFactory.getLogger(HelloServiceController.class);
	@Test
	@Command(fallbackType="com.nio.service.HelloService")
	public void helloService() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		ZrpcClient client = new ZrpcClient();

	}
	
	public User fall(){
		User user = new User();
		user.setAge(0);
		user.setName("0");
		return user;
	}
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException {
		ZrpcClient client = new ZrpcClient();
		client.StartClient("com/nio/consumer/consumer.xml");
		
		//服务的ID和负载均衡策略
		//HelloService service = (HelloService) Client.refer(HelloService.class);
		HelloService service = (HelloService) client.getBean("helloService");
		
//		String sayHello = service.sayHello("zrp");
//		log.info(sayHello);
		User createUser = service.createUser("zhangrp",12);
		log.info("返回的user:"+createUser);
		
	}
}
