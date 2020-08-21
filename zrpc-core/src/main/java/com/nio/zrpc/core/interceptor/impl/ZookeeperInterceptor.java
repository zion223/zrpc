package com.nio.zrpc.core.interceptor.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

import com.esotericsoftware.minlog.Log;
import com.nio.zrpc.core.interceptor.ServiceInterceptor;
import com.nio.zrpc.definition.RpcRequest;
import com.nio.zrpc.server.ZrpcServer;
import com.nio.zrpc.tag.definition.ZkServiceDefinition;

public class ZookeeperInterceptor implements ServiceInterceptor {

	private RpcRequest def;

	public ZookeeperInterceptor(RpcRequest def) {
		this.def = def;
	}

	/**
	 * @category 从zookeeper中根据接口名找到实现类构造实现类,调用方法,返回结果
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	@Override
	public Object doIntercepptor() throws IOException, ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			NoSuchMethodException, SecurityException {
		//从Spring容器中获取bean
		ApplicationContext context = ZrpcServer.getApplicationContext();
		String interfaceName = def.getInterfaceName();
		ZkServiceDefinition zkd = (ZkServiceDefinition)context.getBean(interfaceName);
		Object bean = context.getBean(zkd.getRef());
		
		
		Log.info("调用服务:"+bean);
		//zkClient.close();
		//String refName="com.nio.service.impl.HelloServiceImpl";
		//Class<?> refClazz = Class.forName(refName.toString());
		Object[] parameterTypes = def.getParameterTypes();
		
		Class[] parameterType = new Class[parameterTypes.length];
		
		for (int i = 0; i < parameterTypes.length; i++) {
			parameterType[i] = (Class) Class.forName(StringUtils.substring(parameterTypes[i].toString(),6));
		}
		Class<?> clazz = Class.forName(interfaceName);
		
		Method method = clazz.getDeclaredMethod(def.getMethodName(), parameterType);
		Object result = null;
		try {
			result = method.invoke(bean, def.getArguments());
		} catch (IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return result;
	}
	


}
