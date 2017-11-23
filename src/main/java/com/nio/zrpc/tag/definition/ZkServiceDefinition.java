package com.nio.zrpc.tag.definition;

public class ZkServiceDefinition {

	private String id;
	//接口名
	private String InterfaceName;
	//实现类
	private String ref;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInterfaceName() {
		return InterfaceName;
	}
	public void setInterfaceName(String interfaceName) {
		InterfaceName = interfaceName;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}

	

}
