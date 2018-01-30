package com.nio.consumer.test;
import com.esotericsoftware.minlog.Log;
import com.nio.entity.User;
import com.nio.service.HelloService;
import com.nio.zrpc.client.ZrpcClient;


public class Client {

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException {
	

		//发送序列化好的对象
		ZrpcClient client = new ZrpcClient();
		client.StartClient("com/nio/consumer/consumer.xml");
		HelloService service = (HelloService) client.getBean("helloService");
		
//		String sayHello = service.sayHello("zrp");
//		Log.info(sayHello);
		User user = service.createUser("zrp", 21);
		Log.info(user.getName()+user.getAge());
		
	}

	
	
}
