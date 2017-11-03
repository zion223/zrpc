package com.nio.consul.strategy;

import java.util.Map;

public interface LoadBalanceStrategy {
	public String getInstance(Map<String,Integer> map);

}
