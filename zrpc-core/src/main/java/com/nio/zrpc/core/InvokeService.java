package com.nio.zrpc.core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.nio.zrpc.registry.RegistryType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nio.zrpc.core.interceptor.impl.ZookeeperInterceptor;
import com.nio.zrpc.core.interceptor.impl.ConsulInterceptor;
import com.nio.zrpc.definition.RpcRequest;
import com.nio.zrpc.server.ZrpcServer;

public class InvokeService {

    private static final Logger log = LoggerFactory.getLogger(InvokeService.class);

    // 维护一个服务实例Map
    //public static ConcurrentHashMap<String, Object> services = new ConcurrentHashMap<String, Object>();

    public static Object invoke(RpcRequest definition)
            throws ClassNotFoundException, NoSuchMethodException,
            SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, IOException, InstantiationException {

        return ZrpcServer.registryFlag.getValue() == RegistryType.CONSULE_ID ? new ConsulInterceptor(definition).doIntercepptor() : new ZookeeperInterceptor(definition).doIntercepptor();
    }

}
