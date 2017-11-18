package com.nio.entity;

public class User {

	private String name;
	private Integer age;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public User() {
		super();
	}



	@Override
	public String toString() {
		return "User [name=" + name + ", age=" + age + "]";
	}
	public Integer getAge() {
		return age;
	}
	public User(String name, Integer age) {
		super();
		this.name = name;
		this.age = age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
}
