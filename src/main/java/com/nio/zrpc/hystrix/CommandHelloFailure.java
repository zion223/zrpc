package com.nio.zrpc.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class CommandHelloFailure extends HystrixCommand<Object> {

	protected CommandHelloFailure(String name) {
		super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
	}

	@Override
	protected Object run() throws Exception {
		throw new RuntimeException("this command will always fail");
	}
	@Override
	protected Object getFallback() {
		System.out.println("=============Fallback============");
		return "Hello failure";
	}
	public static void main(String[] args) {
		new CommandHelloFailure("example").execute();
	}

}

