package com.nio.zrpc.core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nio.zrpc.consul.ConsulUtil;
import com.nio.zrpc.consul.OkHttp3ClientManager;
import com.nio.zrpc.consul.request.ServiceRequest;
import com.nio.zrpc.consul.strategy.HashStrategy;
import com.nio.zrpc.definition.RpcDefinition;
import com.nio.zrpc.server.Server;
import com.nio.zrpc.util.StringUtil;

public class InvokeService {
	private static final Logger log = LoggerFactory.getLogger(InvokeService.class);

	// 维护一个服务实例Map
	//public static ConcurrentHashMap<String, Object> services = new ConcurrentHashMap<String, Object>();

	public static Object invokeService(RpcDefinition definition)
			throws ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, IOException {
		//log.info(definition);
		// received
		// message:{"arguments":["zhangrp","12"],"interfaceName":"com.nio.service.HelloService",
		// "methodName":"createUser",
		// "parameterTypes":["java.lang.String","java.lang.String"]}

		String serviceName = StringUtil.toLowerCaseFirstOne(definition.getInterfaceName()
				.substring(definition.getInterfaceName().lastIndexOf(".") + 1));
		log.info("调用的服务:"+serviceName);
		//负载均衡策略
		ServiceRequest request = new ConsulUtil(Server.getRegistryAddress()).serviceGet(serviceName,
				new HashStrategy());

		OkHttp3ClientManager manger = OkHttp3ClientManager.getInstance();

		int argLength = definition.getArguments().length;

		Map<String, Object> hashMap = new HashMap<String, Object>();
		for(int i=0;i<argLength;i++){
			hashMap.put("param"+i, definition.getArguments()[i]);
		}
		
		// User response
		// =manger.getBeanExecute("http:"+request.getAddress()+":"+request.getPort()+"/"+definition.getMethodName(),
		// hashMap, User.class);
		// log.info(response.getAge());
		// http:127.0.0.1:8082/sayHello?&param=[Ljava.lang.Object;@17e4fe6
		String responseString = manger.getStringExecute(
				"http:" + request.getAddress() + ":" + request.getPort() + "/"
						+ definition.getMethodName(), hashMap);
		
		

		// Class<?> serviceinterfaceclass = Class.forName(definition
		// .getInterfaceName());
		//
		// Object service = services.get(definition.getInterfaceName());
		//
		// Object[] parameterTypes = definition.getParameterTypes();
		//
		// int paramLength = parameterTypes.length;
		// //Class[] paramTypes=new Class[a]{};
		// Class[] parameterType = new Class[paramLength];
		// for(int i=0;i<paramLength;i++){
		// parameterType[i]=(Class)Class.forName(parameterTypes[i].toString());
		// }
		//
		// Method method = serviceinterfaceclass.getDeclaredMethod(
		// definition.getMethodName(), parameterType);
		// method.setAccessible(true);
		// Object result = method.invoke(service, definition.getArguments());
		return responseString;

	}

	

}
