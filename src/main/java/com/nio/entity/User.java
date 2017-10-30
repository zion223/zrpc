package com.nio.entity;

public class User {

	private String name;
	private String age;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", age=" + age + "]";
	}
	public String getAge() {
		return age;
	}
	public User(String name, String age) {
		super();
		this.name = name;
		this.age = age;
	}
	public void setAge(String age) {
		this.age = age;
	}
}
