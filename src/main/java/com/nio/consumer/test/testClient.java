package com.nio.consumer.test;
import com.esotericsoftware.minlog.Log;
import com.nio.entity.User;
import com.nio.service.HelloService;
import com.nio.zrpc.client.ZrpcClient;


public class testClient {

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException {
	

		//发送序列化好的对象
		ZrpcClient client = new ZrpcClient();
		client.ZrpcClient("127.0.0.1:8000","com/nio/consumer/consumer.xml");
		HelloService service = (HelloService) ZrpcClient.getBean("helloService");
		
		//String sayHello = service.sayHello("zrp");
		User user = service.createUser("zrp", 21);
		Log.info(user.getName()+user.getAge());
		
	}

	
	
}
