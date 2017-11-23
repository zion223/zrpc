package com.nio.zrpc.core.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nio.zrpc.definition.RpcDefinition;
import com.nio.zrpc.registry.consul.ConsulUtil;
import com.nio.zrpc.registry.consul.OkHttp3ClientManager;
import com.nio.zrpc.registry.consul.request.ServiceRequest;
import com.nio.zrpc.registry.consul.strategy.HashStrategy;
import com.nio.zrpc.server.ZrpcServer;
import com.nio.zrpc.util.StringUtil;

public class ConsulInterceptor implements ServiceInterceptor {
	private static final Logger log = LoggerFactory.getLogger(ConsulInterceptor.class);
	private RpcDefinition def;
	public ConsulInterceptor(RpcDefinition defination){
		this.def=defination;
	}
	
	@Override
	public String doIntercepptor() throws IOException {
		String serviceName = StringUtil.toLowerCaseFirstOne(def.getInterfaceName()
				.substring(def.getInterfaceName().lastIndexOf(".") + 1));
		log.info("调用的服务:"+serviceName);
		//负载均衡策略
		ServiceRequest request = new ConsulUtil(ZrpcServer.getRegistryAddress()).serviceGet(serviceName,
				new HashStrategy());

		OkHttp3ClientManager manger = OkHttp3ClientManager.getInstance();

		int argLength = def.getArguments().length;
		// TODO 调用的方法参数必须固定(优化)
		Map<String, Object> hashMap = new HashMap<String, Object>();
		for(int i=0;i<argLength;i++){
			hashMap.put("param"+i, def.getArguments()[i]);
		}
		
		
		String responseString = manger.getStringExecute(
				"http:" + request.getAddress() + ":" + request.getPort() + "/"
						+ def.getMethodName(), hashMap);
		return responseString;
	}

	
}
