package com.nio.service;

import com.nio.entity.User;

public interface HelloService {
    public String sayHello(String param);

    public String sayHi(String param);

    public User createUser(String name, Integer age);
}
