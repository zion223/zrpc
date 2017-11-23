package com.nio.zrpc.registry.consul;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.HostAndPort;
import com.nio.entity.User;
import com.nio.zrpc.hystrix.anno.Command;
import com.nio.zrpc.registry.consul.entity.ServiceRegisterDefinition;
import com.nio.zrpc.registry.consul.request.ServiceRequest;
import com.nio.zrpc.registry.consul.strategy.HashStrategy;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.KeyValueClient;
import com.orbitz.consul.cache.ConsulCache;
import com.orbitz.consul.cache.ServiceHealthCache;
import com.orbitz.consul.cache.ServiceHealthKey;
import com.orbitz.consul.model.agent.Agent;
import com.orbitz.consul.model.health.ServiceHealth;

public class ConsulTest {
	private static final Logger log = LoggerFactory.getLogger(ConsulTest.class);

	static Consul consul=Consul.builder().withHostAndPort(HostAndPort.fromString("127.0.0.1:8500")).build();
	@Test
	public void testAnno() throws NoSuchMethodException, SecurityException, ClassNotFoundException{
		
		Method method = Class.forName("com.nio.consul.ConsulUtil").getDeclaredMethod("testGet");
		Command command = method.getAnnotation(Command.class);
		log.info(command.fallbackMethod());
		log.info(command.fallbackType());
		
	}

	
	/**
	 * @throws Exception
	 * @deprecated 暂时不可用
	 */
	@Test
	public void testSubscribe() throws Exception{
		Agent agent = consul.agentClient().getAgent();
		String serviceName = "movieService";
		HealthClient healthClient = consul.healthClient();  
		ServiceHealthCache svHealth = ServiceHealthCache.newCache(healthClient, serviceName);

		svHealth.addListener(new ConsulCache.Listener<ServiceHealthKey, ServiceHealth>() {

			@Override
			public void notify(Map<ServiceHealthKey, ServiceHealth> newValues) {
				log.info(newValues.toString());	
			}
		});
		svHealth.start();
	}
	
	@Test
	public void testRegister() throws IOException {  
		//构造需要注册的服务对象Definition
	
		 //更改ID可同时注册多个服务
		 ServiceRegisterDefinition definition = new ServiceRegisterDefinition("helloService", "HelloService","defaultTag", "127.0.0.1", "8082 8081");
		 ConsulUtil consulUtil = new ConsulUtil("127.0.0.1:8500");
		 consulUtil.serviceRegister(definition);  
		
		
        
    }  
	@Test
	public void testKV(){
		
		KeyValueClient kvClient = consul.keyValueClient();

		kvClient.putValue("foo", "bar");

		String value = kvClient.getValueAsString("foo").get(); // bar
		log.info("foo:"+value);
		
	}
	@Test
	//@Command(fallbackType="com.nio.consul.ConsulUtil")
	public void testGet() throws IOException{
		//根据服务名 找到在注册中心注册的实例  负载均衡
		ConsulUtil consulUtil = new ConsulUtil("127.0.0.1:8500");
        ServiceRequest serviceGet = consulUtil.serviceGet("helloService",new HashStrategy());
        
       
        //构造服务请求对象 通过OkHttp发送请求
        OkHttp3ClientManager manger = OkHttp3ClientManager.getInstance();
        Map<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("param0", "zhang");
        hashMap.put("param1", "21");
        User response =manger.getBeanExecute("http:"+serviceGet.getAddress()+":"+serviceGet.getPort()+"/createUser", hashMap, User.class);
       	log.info(response.toString());
       	
	}
}
