package com.nio.consul;

import java.util.List;

import com.google.common.net.HostAndPort;
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
	
	public static void serviceRegister(){
		AgentClient agent = consul.agentClient();

		ImmutableRegCheck check = ImmutableRegCheck.builder().http("http://127.0.0.1:8081/health").interval("5s").build();
		
		Builder builder = ImmutableRegistration.builder();
		//builder.id("tomcat").name("tomcatService").addTags("v1").address("192.168.152.132").port(8080).addChecks(check);
		builder.id("movieService").name("movieService").addTags("v1").address("127.0.0.1").port(8081).addChecks(check);
		agent.register(builder.build());
	}
	
	public static void serviceGet(String name) {  
        HealthClient client = consul.healthClient();  
        ConsulResponse object= client.getAllServiceInstances(name);
        List<ImmutableServiceHealth> serviceHealths=(List<ImmutableServiceHealth>)object.getResponse();
        for(ImmutableServiceHealth serviceHealth:serviceHealths){
           System.out.println(serviceHealth.getService().getAddress());
           System.out.println(serviceHealth.getService().getPort());
           System.out.println(serviceHealth.getService().getService());
           System.out.println(serviceHealth.getService().getTags());
            
        }
        //获取所有服务个数 
        //System.out.println(client.getAllServiceInstances(name).getResponse().size());  
          
        //获取所有正常的服务（健康检测通过的）  
        client.getHealthyServiceInstances(name).getResponse().forEach((resp) -> {  
            System.out.println(resp);  
        });  
    }  
	
	public static void main(String[] args) {  
        //serviceRegister();  
        serviceGet("movieService");  
    }  
}
