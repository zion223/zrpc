package com.nio.zrpc.core.interceptor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.I0Itec.zkclient.ZkClient;

import com.nio.zrpc.definition.RpcDefinition;
import com.nio.zrpc.registry.zookeeper.ZkConstant;
import com.nio.zrpc.registry.zookeeper.ZookeeperUtil;
import com.nio.zrpc.server.ZrpcServer;

public class ZookeeperInterceptor implements ServiceInterceptor {

	private RpcDefinition def;

	public ZookeeperInterceptor(RpcDefinition def) {
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
		String interfaceName = def.getInterfaceName();
		
		//ZkClient zkClient = new ZkClient(ZrpcServer.getRegistryAddress());
		ZkClient zkClient = ZookeeperUtil.getInstance();
		Object refName = zkClient.readData(ZkConstant.ROOT_TAG + "/"
				+ interfaceName);
		zkClient.close();
		//String refName="com.nio.service.impl.HelloServiceImpl";
		Class<?> refClazz = Class.forName(refName.toString());
		Object[] parameterTypes = def.getParameterTypes();

		Object refClass = refClazz.newInstance();
		Class[] parameterType = new Class[parameterTypes.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			parameterType[i] = (Class) Class.forName(parameterTypes[i]
					.toString());
		}
		Method method = refClazz.getDeclaredMethod(def.getMethodName(), parameterType);
		Object result = null;
		try {
			result = method.invoke(refClass, def.getArguments());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return result;
	}

}
