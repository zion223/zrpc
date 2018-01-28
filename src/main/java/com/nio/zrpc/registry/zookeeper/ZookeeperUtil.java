package com.nio.zrpc.registry.zookeeper;

import org.I0Itec.zkclient.ZkClient;


public class ZookeeperUtil {

	private static ZkClient zkClient;
	private ZookeeperUtil() {}  

    //静态工厂方法   
    public static ZkClient getInstance() {  
         if (zkClient == null) {  
        	 //TODO ZK服务器地址
        	 zkClient = new ZkClient("192.168.252.144:2181");  
         }    
        return zkClient;  
    }  
}
