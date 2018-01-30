package com.nio.zrpc.registry.strategy;

import org.apache.commons.lang.StringUtils;

public class FindLoadBalanceStrategy {

	public static LoadBalanceStrategy getStrategyByName(String name){
		if(StringUtils.equals(name, "hash")){
			return new HashStrategy();
		}else if(StringUtils.equals(name, "random")){
			return new RandomStrategy();
		}
		return null;
	}
}
