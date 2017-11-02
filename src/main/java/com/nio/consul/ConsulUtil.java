package com.nio.consul;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.net.HostAndPort;
import com.nio.consul.entity.ServiceRegisterDefinition;
import com.nio.consul.request.ServiceRequest;
import com.nio.entity.User;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.model.ConsulResponse;
import com.orbitz.consul.model.agent.ImmutableRegCheck;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.ImmutableRegistration.Builder;
import com.orbitz.consul.model.health.ImmutableServiceHealth;

public class ConsulUtil {

	static Consul consul=Consul.builder().withHostAndPort(HostAndPort.fromString("127.0.0.1:8500")).build();
	
	public static void serviceRegister(ServiceRegisterDefinition definition){
		AgentClient agent = consul.agentClient();

		ImmutableRegCheck check = ImmutableRegCheck.builder().http("http://"+definition.getAdress()+":"+definition.getPort()+"/health").interval("5s").build();
		
		Builder builder = ImmutableRegistration.builder();
		//builder.id("tomcat").name("tomcatService").addTags("v1").address("192.168.152.132").port(8080).addChecks(check);
		builder.id(definition.getId()).name(definition.getName()).addAllTags(definition.getTag()).address(definition.getAdress()).port(definition.getPort()).addChecks(check);
		agent.register(builder.build());
	}
	
	public static ServiceRequest serviceGet(String name) {  
        HealthClient client = consul.healthClient();  
        ConsulResponse object= client.getAllServiceInstances(name);
        List<ImmutableServiceHealth> serviceHealths=(List<ImmutableServiceHealth>)object.getResponse();
        String address = null;
        int port=0;
        List<String> tag=null;
        String serviceName=null;
        //多个服务实例  
        
        
        //load balance
        for(ImmutableServiceHealth serviceHealth:serviceHealths){
           System.out.println(address=serviceHealth.getService().getAddress());
           System.out.println(port=serviceHealth.getService().getPort());
           System.out.println(serviceName=serviceHealth.getService().getService());
           System.out.println(tag=serviceHealth.getService().getTags());
             
        }
        //获取所有服务个数 
        //System.out.println(client.getAllServiceInstances(name).getResponse().size());  
          
        //获取所有正常的服务（健康检测通过的）  
        client.getHealthyServiceInstances(name).getResponse().forEach((resp) -> {  
            System.out.println(resp);  
        });  
        return new ServiceRequest(address, port, tag, serviceName);
    }  
	
	public static void main(String[] args) throws IOException {  
		//构造需要注册的服务对象Definition
		
        //serviceRegister();  
		
		//根据服务名 找到在注册中心注册的实例  负载均衡
        ServiceRequest serviceGet = serviceGet("movieService");
        
        //构造服务请求对象 通过OkHttp发送请求
        OkHttp3ClientManager manger = OkHttp3ClientManager.getInstance();
        Map<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("name", "zhang");
        hashMap.put("age", "21");
        User response =manger.getBeanExecute("http:"+serviceGet.getAddress()+":"+serviceGet.getPort()+"/createUser", hashMap, User.class);
       	System.out.println(response.getAge());
        
    }  
}
