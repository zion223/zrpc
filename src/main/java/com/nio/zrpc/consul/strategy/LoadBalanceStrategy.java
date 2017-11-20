package com.nio.zrpc.consul.strategy;

import java.util.Map;

public interface LoadBalanceStrategy {
	public String getInstance(Map<String,Integer> map);

}
