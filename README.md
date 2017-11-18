# zrpc
zrpc是一款简洁易用的分布式服务化治理框架。
***
## 特性
- 使用自定义标签与Spring整合
- 支持spring boot应用
- 使用 Consul作为注册中心
- 支持tcp/http协议的服务
- 客户端自动恢复
- 使用Hystrix作为服务保护机制

***

### 服务端
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:jee="http://www.springframework.org/schema/jee"
	xmlns:context="http://www.springframework.org/schema/context" xmlns:p="http://www.springframework.org/schema/p"
	xmlns:zrpc="http://www.lexueba.com/schema/service" xmlns:util="http://www.springframework.org/schema/util"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans.xsd
	http://www.springframework.org/schema/context
	http://www.springframework.org/schema/context/spring-context-4.0.xsd
	http://www.springframework.org/schema/jee
	http://www.springframework.org/schema/jee/spring-jee-4.1.xsd
	http://www.springframework.org/schema/util 
	http://www.springframework.org/schema/util/spring-util-4.1.xsd
	http://www.lexueba.com/schema/service
 	http://www.lexueba.com/schema/service.xsd">

	
	<!-- 
		<dubbo:application name="dubbodemo-provider"/> 
		<dubbo:registry id="dubbodemo" address="zookeeper://localhost:2181"/> 
		<dubbo:protocol name="dubbo" port="28080"/> 
		<dubbo:service registry="dubbodemo" timeout="3000" interface="com.chanshuyi.service.IUserService" ref="userService"/> 
		实例化实现了接口的服务，维护一个map private String interfaceName; 接口名称 
		private String methodName; 方法名称 private Object[] parameterTypes; 方法的参数类型 
		private Object[] arguments; 方法参数 -->

	<!-- 暴露服务接口 -->
	
	<zrpc:service id="hello" interfaceName="com.nio.service.HelloService" ref="helloService" interfaceImplName="com.nio.provider.service.impl.HelloServiceImpl"/>

</beans>
```

#### 启动类
```java
public class TestServer {

	public static void main(String[] args) {
		  //创建服务器    地址和xml文件的路径
		  Server.ZrpcServer(new InetSocketAddress("127.0.0.1",8000),"com/nio/provider/zrpc-provider.xml");
		  
	}
}


```
***

### 客户端
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:zrpc="http://www.lexueba.com/schema/reference"
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
	http://www.lexueba.com/schema/reference
 	http://www.lexueba.com/schema/reference.xsd">


<!-- 
	<dubbo:application name="dubbodemo-consumer"/>
	 
    <dubbo:registry address="zookeeper://localhost:2181"/>
    
    <dubbo:protocol port="28080"/>
    
    <dubbo:reference id="userService" interface="com.chanshuyi.service.IUserService"/>

 -->
	<zrpc:reference id="helloService" interfaceName="com.nio.service.HelloService"/>
	
</beans>
```
#### 启动类

```java
public class testClient {

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, ClassNotFoundException {
	

		//发送序列化好的对象
		Client.ZrpcClient(new InetSocketAddress("127.0.0.1",8000));
		HelloService service = Client.refer(HelloService.class);
		//String result = service.sayHello("Zrp");
		//System.out.println("result:"+result);
		
//		User createUser = service.createUser("zhangrp", "12");
//		System.out.println("返回的user:"+createUser);
		
		//String resultHi = service.sayHi("ZRP");
		//System.out.println(createUser);
		
	}
	
	
}
```

