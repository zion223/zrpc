package com.nio.zrpc.tag.definition;

public class ZkServiceDefinition {

	//接口名
	private String InterfaceName;
	//由Spring实例化的服务实现类Bean
	private String ref;

	private String id;

	/**
	 * 暴露的服务端口
	 */
	private String port;

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

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
