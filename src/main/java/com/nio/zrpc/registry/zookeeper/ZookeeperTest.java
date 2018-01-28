package com.nio.zrpc.registry.zookeeper;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.I0Itec.zkclient.ZkClient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nio.service.HelloService;

public class ZookeeperTest {
	static final String CONNECT_ADDR = "192.168.252.144:2181";
	
	private static final Logger log = LoggerFactory.getLogger(ZookeeperTest.class);
	
	private static final String ROOT_TAG ="/zrpc";
	@Test
	public void testAddress() throws UnknownHostException{
		System.out.println(InetAddress.getLocalHost().getHostAddress().toString());
	}
	
	@Test
	public void testConn() throws Exception {
		
		ZkClient zkc = new ZkClient(CONNECT_ADDR);
		log.info("Connected!!!!");
		/**
		 * zk中存放的内容
		 * interfaceName  -->impl
		 * 
		 */
		
		zkc.createPersistent(ROOT_TAG);
		//interfaceName 和impl
		
		
		zkc.createPersistent(ROOT_TAG+"/com.nio.service.HelloService","com.nio.service.impl.HelloServiceImpl");
		Object readData = zkc.readData("/zrpc/com.nio.service.HelloService");
		
		
		
		
		Class<?> clazz = Class.forName(readData.toString());
		HelloService instance = (HelloService) clazz.newInstance();
		String sayHello = instance.sayHello("zrp");
		log.info(sayHello);
		
		//1. create and delete方法 
//		zkc.createEphemeral("/temp");
//		zkc.createPersistent("/super/c1", true);
//		Thread.sleep(10000);
//		zkc.delete("/temp");
//		zkc.deleteRecursive("/super");
		//2. 设置path和data 并且读取子节点和每个节点的内容
//		zkc.createPersistent("/super", "1234");
//		zkc.createPersistent("/super/c1", "c1内容");
//		zkc.createPersistent("/super/c2", "c2内容");
//		List<String> list = zkc.getChildren("/super");
//		for(String p : list){
//			System.out.println(p);
//			String rp = "/super/" + p;
//			String data = zkc.readData(rp);
//			log.info("节点为：" + rp + "，内容为: " + data);
//		}
		
		//3. 更新和判断节点是否存在
//		zkc.writeData("/super/c1", "新内容");
//		System.out.println(zkc.readData("/super/c1"));
//		System.out.println(zkc.exists("/super/c1"));
		
		//4.递归删除/super内容
		zkc.deleteRecursive(ROOT_TAG);		
		zkc.close();
	}
}
