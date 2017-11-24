package com.nio.zrpc.registry.consul;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.HostAndPort;
import com.nio.zrpc.registry.consul.entity.ServiceRegisterDefinition;
import com.nio.zrpc.registry.consul.request.ServiceRequest;
import com.nio.zrpc.registry.consul.strategy.LoadBalanceStrategy;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.model.ConsulResponse;
import com.orbitz.consul.model.agent.ImmutableRegCheck;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.ImmutableRegistration.Builder;
import com.orbitz.consul.model.health.ImmutableServiceHealth;

public class ConsulUtil {
	//https://github.com/OrbitzWorldwide/consul-client
	//consul 地址127.0.0.1:8500从配置文件中获取
	//static Consul consul=Consul.builder().withHostAndPort(HostAndPort.fromString("127.0.0.1:8500")).build();
	private static final Logger log = LoggerFactory.getLogger(ConsulUtil.class);
	//设计成单实例模式
	
	static Consul consul;
	public ConsulUtil(String address){
		if(address.equals("")){
			log.error("注册中心的地址呢???");
			throw new NullPointerException("注册中心的地址呢???");
		}
		consul=Consul.builder().withHostAndPort(HostAndPort.fromString(address)).build();
	}

	public void serviceRegister(ServiceRegisterDefinition definition){
		AgentClient agent = consul.agentClient();

		
		Builder builder = ImmutableRegistration.builder();
		//builder.id("tomcat").name("tomcatService").addTags("v1").address("192.168.152.132").port(8080).addChecks(check);
		for(Integer port:definition.getPort()){
			// TODO 这里的服务检查间隔时间可设置
			ImmutableRegCheck check = ImmutableRegCheck.builder().http("http://"+definition.getAdress()+":"+port+"/health").interval("10s").build();
			String id=definition.getId()+Integer.lowestOneBit(port);
			
			builder.id(id).name(definition.getName()).addAllTags(definition.getTag()).address(definition.getAdress()).port(port).addChecks(check);
			agent.register(builder.build());
		}
		
		
	}
	
	public ServiceRequest serviceGet(String name,LoadBalanceStrategy strategy) {  
        HealthClient client = consul.healthClient();  
        @SuppressWarnings("rawtypes")
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
       
        log.info("Server Address:"+server);
        //获取所有服务个数 
        log.info("所有可用的服务的个数为:"+client.getAllServiceInstances(name).getResponse().size());  
          
        //获取所有正常的服务（健康检测通过的）  
        client.getHealthyServiceInstances(name).getResponse().forEach((resp) -> {  
            //log.info(resp);  
        });  
        address=server.subSequence(0, server.lastIndexOf(":")).toString();
        port=Integer.parseInt(server.substring(server.lastIndexOf(":")+1).toString());
        return new ServiceRequest(address, port, tag, serviceName);
    }  

	
	
	
}
