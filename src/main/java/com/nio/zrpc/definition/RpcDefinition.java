package com.nio.zrpc.definition;

import java.io.Serializable;
import java.util.Arrays;

import com.nio.zrpc.hystrix.FallBackDefinition;

public class RpcDefinition implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String interfaceName;

	private String methodName;
	private Object[] parameterTypes;
	private Object[] arguments;
	private FallBackDefinition def;
	public RpcDefinition(String interfaceName, String methodName,
			Object[] parameterTypes, Object[] arguments,FallBackDefinition def) {
		super();
		this.interfaceName = interfaceName;
		this.methodName = methodName;
		this.parameterTypes = parameterTypes;
		this.arguments = arguments;
		this.def=def;
	}

	public FallBackDefinition getDef() {
		return def;
	}

	public void setDef(FallBackDefinition def) {
		this.def = def;
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

}
