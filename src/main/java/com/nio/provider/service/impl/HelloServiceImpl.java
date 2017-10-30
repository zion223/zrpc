package com.nio.provider.service.impl;

import com.nio.entity.User;
import com.nio.service.HelloService;
public class HelloServiceImpl implements HelloService{

	public String sayHello(String param) {
		return "Hello"+param;
	}

	public String sayHi(String param) {
		return "Hi"+param;
	}

	public User createUser(String name, String age) {
		
		return new User(name,age);
	}

}
