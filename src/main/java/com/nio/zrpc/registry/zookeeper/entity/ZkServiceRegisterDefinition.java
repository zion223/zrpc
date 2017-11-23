package com.nio.zrpc.registry.zookeeper.entity;

public class ZkServiceRegisterDefinition {

	private String interfaceName;
	private String ref;
	public String getInterfaceName() {
		return interfaceName;
	}
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public ZkServiceRegisterDefinition(String interfaceName, String ref) {
		super();
		this.interfaceName = interfaceName;
		this.ref = ref;
	}
}
