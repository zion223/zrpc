package com.nio.service.impl;

import com.nio.entity.User;
import com.nio.service.HelloService;

public class HelloServiceImpl implements HelloService{

	@Override
	public String sayHello(String param) {
		return "Hello"+param;
	}

	@Override
	public String sayHi(String param) {
		
		return "Hi"+param;
	}

	@Override
	public User createUser(String name, Integer age) {
		
		return new User(name,age);
	}

}
