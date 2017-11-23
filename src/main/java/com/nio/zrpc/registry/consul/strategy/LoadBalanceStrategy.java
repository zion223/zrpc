package com.nio.zrpc.registry.consul.strategy;

import java.util.Map;

public interface LoadBalanceStrategy {
	public String getInstance(Map<String,Integer> map);

}
