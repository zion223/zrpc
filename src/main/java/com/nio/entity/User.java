package com.nio.entity;

import java.io.Serializable;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
