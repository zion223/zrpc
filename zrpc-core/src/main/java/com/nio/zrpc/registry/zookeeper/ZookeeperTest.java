package com.nio.zrpc.registry.zookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ZookeeperTest {
    private static final String CONNECT_ADDR = "192.168.1.54:2181";

    private static final Logger log = LoggerFactory.getLogger(ZookeeperTest.class);


    @Test
    public void testConn() throws Exception {

        ZkClient zkc = new ZkClient(CONNECT_ADDR);
        log.info("Connected!!!!");
        /*
         * zk中存放的内容
         * interfaceName  -->impl
         *
         */

        //zkc.createPersistent(ROOT_TAG+"/com.nio.service.HelloService","com.nio.service.impl.HelloServiceImpl");
//		ArrayList<String> list = (ArrayList<String>) zkc.getChildren("/zrpc/com.nio.service.HelloService");
//		//[192.168.100.3:8080, 192.168.100.3:8000]
//		int nextInt = RandomUtils.nextInt(list.size());
//
//		System.out.println(list.get(nextInt));


        //1. create and delete方法
        zkc.createEphemeral("/temp");
        zkc.createPersistent("/super/c1", true);
        Thread.sleep(10000);
        zkc.delete("/temp");
        zkc.deleteRecursive("/super/c1");
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
        //zkc.deleteRecursive(ROOT_TAG);
        zkc.close();
    }
}
