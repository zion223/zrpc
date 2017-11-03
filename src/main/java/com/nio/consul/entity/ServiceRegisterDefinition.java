package com.nio.consul.entity;

import java.util.List;

public class ServiceRegisterDefinition {

	private String id;
	private String name;
	private List<String> tag;
	private String adress;
	private int port;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public ServiceRegisterDefinition(String id, String name, List<String> tag,
			String adress, int port) {
		super();
		this.id = id;
		this.name = name;
		this.tag = tag;
		this.adress = adress;
		this.port = port;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getTag() {
		return tag;
	}
	public void setTag(List<String> tag) {
		this.tag = tag;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	
	
}
