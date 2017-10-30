package com.nio.zrpc.definition;

import java.util.Arrays;

public class RpcDefinition {

	private String interfaceName;

	private String methodName;
	private Object[] parameterTypes;
	private Object[] arguments;
	
	public String getInterfaceName() {
		
		return interfaceName;
	}

	public RpcDefinition() {
		super();
	}





	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}



	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}



	public RpcDefinition(String interfaceName, String methodName,
			Object[] parameterTypes, Object[] arguments) {
		super();
		this.interfaceName = interfaceName;
		this.methodName = methodName;
		this.parameterTypes = parameterTypes;
		this.arguments = arguments;
	}

	public Object[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(Object[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public Object[] getArguments() {
		return arguments;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

}
