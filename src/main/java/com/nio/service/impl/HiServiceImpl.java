package com.nio.service.impl;

import com.nio.service.HiService;

public class HiServiceImpl implements HiService {

	@Override
	public String sayHello(String param) {
		return "HiServiceImpl"+param;
	}

	@Override
	public String sayHi(String param) {
		return "HiServiceImpl"+param;
	}

}
