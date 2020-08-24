package com.nio.zrpc.registry.zookeeper;

import org.I0Itec.zkclient.ZkClient;

public class ZookeeperUtil {

	private static ZkClient zkClient;
	private ZookeeperUtil( ) {}  

    //静态工厂方法   
    public static ZkClient getInstance(String path) {
        if (zkClient == null) {
            //TODO ZK服务器地址  只能在这配置吗
            zkClient = new ZkClient(path);
         }
        return zkClient;  
    }
}
