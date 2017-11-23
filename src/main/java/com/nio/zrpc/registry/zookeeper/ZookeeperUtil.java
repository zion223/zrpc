package com.nio.zrpc.registry.zookeeper;

import org.I0Itec.zkclient.ZkClient;

import com.nio.zrpc.server.ZrpcServer;

public class ZookeeperUtil {

	private static ZkClient zkClient;
	private ZookeeperUtil() {}  

    //静态工厂方法   
    public static ZkClient getInstance() {  
         if (zkClient == null) {    
        	 zkClient = new ZkClient(ZrpcServer.getRegistryAddress());  
         }    
        return zkClient;  
    }  
}
