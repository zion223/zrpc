package com.nio.zrpc.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import com.nio.provider.service.impl.HelloServiceImpl;
import com.nio.zrpc.definition.RpcDefinition;

public class InvokeService {
	// 维护一个服务实例Map
	public static ConcurrentHashMap<String, Object> services = new ConcurrentHashMap<String, Object>();

	public static Object invokeService(RpcDefinition definition)
			throws ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Class<?> serviceinterfaceclass = Class.forName(definition
				.getInterfaceName());
		
		Object service = services.get(definition.getInterfaceName());
		
		Object[] parameterTypes = definition.getParameterTypes();
		
		int paramLength = parameterTypes.length;
		//Class[] paramTypes=new Class[a]{};
		Class[] parameterType = new Class[paramLength];
		for(int i=0;i<paramLength;i++){
			parameterType[i]=(Class)Class.forName(parameterTypes[i].toString());
		}
	
		Method method = serviceinterfaceclass.getDeclaredMethod(
				definition.getMethodName(), parameterType);
		method.setAccessible(true);
		Object result = method.invoke(service, definition.getArguments());
		return result;
	
	}


}
