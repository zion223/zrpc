package com.nio.zrpc.registry.zookeeper;

import org.I0Itec.zkclient.ZkClient;

public class ZookeeperUtil {

	private static ZkClient zkClient;
	private ZookeeperUtil( ) {}  

    //静态工厂方法   
    public static ZkClient getInstance() {
        if (zkClient == null) {
            //TODO ZK服务器地址  只能在这配置吗
            zkClient = new ZkClient("192.168.1.54:2181");
         }    
        return zkClient;  
    }  
}
