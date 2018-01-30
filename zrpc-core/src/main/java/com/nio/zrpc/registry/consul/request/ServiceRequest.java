package com.nio.zrpc.registry.consul.request;

import java.util.List;

public class ServiceRequest {

	private String address;
	private int port;
	private List<String> tag;
	private String serviceName;
	
	private String fallbackType;
	private String fallbackMethod;
	public String getFallbackType() {
		return fallbackType;
	}
	public void setFallbackType(String fallbackType) {
		this.fallbackType = fallbackType;
	}
	public String getFallbackMethod() {
		return fallbackMethod;
	}
	public void setFallbackMethod(String fallbackMethod) {
		this.fallbackMethod = fallbackMethod;
	}
	public ServiceRequest(String address, int port, List<String> tag,
			String serviceName) {
		super();
		this.address = address;
		this.port = port;
		this.tag = tag;
		this.serviceName = serviceName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public List<String> getTag() {
		return tag;
	}
	public void setTag(List<String> tag) {
		this.tag = tag;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
}
