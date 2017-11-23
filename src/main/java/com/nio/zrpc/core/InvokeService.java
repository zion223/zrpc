package com.nio.zrpc.core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nio.zrpc.core.interceptor.ConsulInterceptor;
import com.nio.zrpc.core.interceptor.ZookeeperInterceptor;
import com.nio.zrpc.definition.RpcDefinition;
import com.nio.zrpc.registry.consul.ConsulUtil;
import com.nio.zrpc.registry.consul.OkHttp3ClientManager;
import com.nio.zrpc.registry.consul.request.ServiceRequest;
import com.nio.zrpc.registry.consul.strategy.HashStrategy;
import com.nio.zrpc.server.ZrpcServer;
import com.nio.zrpc.util.StringUtil;

public class InvokeService {
	private static final Logger log = LoggerFactory.getLogger(InvokeService.class);

	// 维护一个服务实例Map
	//public static ConcurrentHashMap<String, Object> services = new ConcurrentHashMap<String, Object>();

	public static Object invokeService(RpcDefinition definition)
			throws ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, IOException, InstantiationException {

		
		Object responseString = null;
		//判断请求处理的方式
		if(ZrpcServer.registryFlag){
			
			//使用Consul处理请求
			responseString= new ConsulInterceptor(definition).doIntercepptor();
		}else{
			responseString = new ZookeeperInterceptor(definition).doIntercepptor();
		}
		
		
		

		
		return responseString;

	}

	

}
