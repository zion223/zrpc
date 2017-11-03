package com.nio.consul;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.Test;

import com.google.common.net.HostAndPort;
import com.nio.consul.entity.ServiceRegisterDefinition;
import com.nio.consul.request.ServiceRequest;
import com.nio.consul.strategy.HashStrategy;
import com.nio.consul.strategy.LoadBalanceStrategy;
import com.nio.consul.strategy.RandomStrategy;
import com.nio.entity.User;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.KeyValueClient;
import com.orbitz.consul.cache.ConsulCache;
import com.orbitz.consul.cache.ServiceHealthCache;
import com.orbitz.consul.cache.ServiceHealthKey;
import com.orbitz.consul.model.ConsulResponse;
import com.orbitz.consul.model.agent.Agent;
import com.orbitz.consul.model.agent.ImmutableRegCheck;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.ImmutableRegistration.Builder;
import com.orbitz.consul.model.health.ImmutableServiceHealth;
import com.orbitz.consul.model.health.ServiceHealth;

public class ConsulUtil {
	//https://github.com/OrbitzWorldwide/consul-client
	static Consul consul=Consul.builder().withHostAndPort(HostAndPort.fromString("127.0.0.1:8500")).build();
	
	public static void serviceRegister(ServiceRegisterDefinition definition){
		AgentClient agent = consul.agentClient();

		ImmutableRegCheck check = ImmutableRegCheck.builder().http("http://"+definition.getAdress()+":"+definition.getPort()+"/health").interval("5s").build();
		
		Builder builder = ImmutableRegistration.builder();
		//builder.id("tomcat").name("tomcatService").addTags("v1").address("192.168.152.132").port(8080).addChecks(check);
		builder.id(definition.getId()).name(definition.getName()).addAllTags(definition.getTag()).address(definition.getAdress()).port(definition.getPort()).addChecks(check);
		agent.register(builder.build());
	}
	
	public static ServiceRequest serviceGet(String name,LoadBalanceStrategy strategy) {  
        HealthClient client = consul.healthClient();  
        ConsulResponse object= client.getAllServiceInstances(name);
        List<ImmutableServiceHealth> serviceHealths=(List<ImmutableServiceHealth>)object.getResponse();
        
        
        String address = null;
        int port=0;
        List<String> tag=null;
        String serviceName=null;
        String server=null;
        //多个服务实例  
        HashMap<String,Integer> hashMap = new HashMap<String ,Integer>();
       
        
        //load balance
        for(ImmutableServiceHealth serviceHealth:serviceHealths){
           address=serviceHealth.getService().getAddress();
           port=serviceHealth.getService().getPort();
           serviceName=serviceHealth.getService().getService();
           tag=serviceHealth.getService().getTags();
           hashMap.put(serviceHealth.getService().getAddress()+":"+serviceHealth.getService().getPort(), 5);
        }
        server=strategy.getInstance(hashMap);
        //负载均衡算法  参数hashMap  返回值 String
       
        System.out.println(server);
        //获取所有服务个数 
        System.out.println(client.getAllServiceInstances(name).getResponse().size());  
          
        //获取所有正常的服务（健康检测通过的）  
        client.getHealthyServiceInstances(name).getResponse().forEach((resp) -> {  
            //System.out.println(resp);  
        });  
        address=server.subSequence(0, server.lastIndexOf(":")).toString();
        port=Integer.parseInt(server.substring(server.lastIndexOf(":")+1).toString());
        return new ServiceRequest(address, port, tag, serviceName);
    }  
	
	@Test
	public void testRegister() throws IOException {  
		//构造需要注册的服务对象Definition
		 ArrayList<String> tagList = new ArrayList<String>();
		 tagList.add("v2");
		 //更改ID可同时注册多个服务
		 ServiceRegisterDefinition definition = new ServiceRegisterDefinition("helloService1", "helloService",tagList, "127.0.0.1", 8081);
		
        serviceRegister(definition);  
		
		
        
    }  
	@Test
	public void testKV(){
		
		KeyValueClient kvClient = consul.keyValueClient();

		kvClient.putValue("foo", "bar");

		String value = kvClient.getValueAsString("foo").get(); // bar
		System.out.println("foo:"+value);
		
	}
	@Test
	public void testGet() throws IOException{
		//根据服务名 找到在注册中心注册的实例  负载均衡
        ServiceRequest serviceGet = serviceGet("helloService",new HashStrategy());
        
        //构造服务请求对象 通过OkHttp发送请求
        OkHttp3ClientManager manger = OkHttp3ClientManager.getInstance();
        Map<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("name", "zhang");
        hashMap.put("age", "21");
        User response =manger.getBeanExecute("http:"+serviceGet.getAddress()+":"+serviceGet.getPort()+"/createUser", hashMap, User.class);
       	System.out.println(response.getAge());
	}
	
	/**
	 * @deprecated 暂时不可用
	 * @throws Exception
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
				System.out.println(newValues.toString());	
			}
		});
		svHealth.start();
	}
	
}
