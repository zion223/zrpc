package com.nio.zrpc.tag.definition;

public class ServiceDefinition {

	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	private String interfaceName;
	private String interfaceImplName;
	public String getInterfaceImplName() {
		return interfaceImplName;
	}
	public void setInterfaceImplName(String interfaceImplName) {
		this.interfaceImplName = interfaceImplName;
	}
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
}
