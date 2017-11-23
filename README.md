# zrpc
zrpc是一款简洁易用的分布式服务化治理框架。
***
## 特性
- 使用自定义标签与Spring整合
- 支持spring boot应用
- 可选择使用Consul或者Zookeeper作为注册中心
- 支持tcp/http协议的服务
- 客户端自动恢复
- 使用Hystrix作为服务保护机制
- 支持服务降级
***

### 服务端
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:jee="http://www.springframework.org/schema/jee"
	xmlns:context="http://www.springframework.org/schema/context" xmlns:p="http://www.springframework.org/schema/p"
	xmlns:zrpc="http://www.lexueba.com/schema/zrpc" xmlns:util="http://www.springframework.org/schema/util"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans.xsd
	http://www.springframework.org/schema/context
	http://www.springframework.org/schema/context/spring-context-4.0.xsd
	http://www.springframework.org/schema/jee
	http://www.springframework.org/schema/jee/spring-jee-4.1.xsd
	http://www.springframework.org/schema/util 
	http://www.springframework.org/schema/util/spring-util-4.1.xsd
	http://www.lexueba.com/schema/zrpc
 	http://www.lexueba.com/schema/zrpc.xsd">

	
	<!-- 
		<dubbo:application name="dubbodemo-provider"/> 
		<dubbo:registry id="dubbodemo" address="zookeeper://localhost:2181"/> 
		<dubbo:protocol name="dubbo" port="28080"/> 
		<dubbo:service registry="dubbodemo" timeout="3000" interface="com.chanshuyi.service.IUserService" ref="userService"/> 
		实例化实现了接口的服务，维护一个map private String interfaceName;  -->

	<!-- 暴露服务接口 -->
	
	<zrpc:service  id="helloServiceId1" name="HelloService" address="127.0.0.1" port="8082 8081"/>	
	
	
	<zrpc:registry id="registry" address="Consul://127.0.0.1:8500"/>

</beans>
```

#### 启动类
```java
public class TestServer {
	private static final Logger log = LoggerFactory.getLogger(TestServer.class);
	public static void main(String[] args) {
		 //创建服务器    地址和xml文件的路径
		  Server.ZrpcServer("127.0.0.1:8000","com/nio/provider/zrpc-provider.xml");
	}
}


```
***

### 客户端
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:zrpc="http://www.lexueba.com/schema/zrpc"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:jee="http://www.springframework.org/schema/jee"
	xmlns:context="http://www.springframework.org/schema/context" xmlns:p="http://www.springframework.org/schema/p"
 	xmlns:util="http://www.springframework.org/schema/util"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans.xsd
	http://www.springframework.org/schema/context
	http://www.springframework.org/schema/context/spring-context-4.0.xsd
	http://www.springframework.org/schema/jee
	http://www.springframework.org/schema/jee/spring-jee-4.1.xsd
	http://www.springframework.org/schema/util 
	http://www.springframework.org/schema/util/spring-util-4.1.xsd
	http://www.lexueba.com/schema/zrpc
 	http://www.lexueba.com/schema/zrpc.xsd">


<!-- 
    <dubbo:application name="dubbodemo-consumer"/>
	 
    <dubbo:registry address="zookeeper://localhost:2181"/>
    
    <dubbo:protocol port="28080"/>
    
    <dubbo:reference id="userService" interface="com.chanshuyi.service.IUserService"/>

 -->
	<zrpc:reference id="helloService" interfaceName="com.nio.service.HelloService" strategy="hash"/>
	<!-- consul 地址127.0.0.1:8500从配置文件中获取 -->
</beans>
```
#### 启动类

```java
public class testClient {
	private static final Logger log = LoggerFactory.getLogger(testClient.class);
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, ClassNotFoundException {
	

		Client client = new Client();
		client.ZrpcClient(new InetSocketAddress("127.0.0.1",8000),"com/nio/consumer/zrpc-consumer.xml");
		
		//服务的ID和负载均衡策略
		//HelloService service = (HelloService) Client.refer(HelloService.class);
		HelloService service = (HelloService) Client.getBean("helloService");
		
		String sayHello = service.sayHello("zrp");
		log.info(sayHello);
//		User createUser = service.createUser("zhangrp",12);
//		log.info("返回的user:"+createUser);
		
	}
	
	
}
```

