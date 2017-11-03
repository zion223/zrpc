package com.nio.consul.strategy;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class RandomStrategy implements LoadBalanceStrategy {

	@Override
	public String  getInstance(Map<String, Integer> hashMap) {
		Set<String> keySet = hashMap.keySet();
		ArrayList<String> keyList = new ArrayList<String>();
		keyList.addAll(keySet);

		Random random = new Random();
		int randomPos = random.nextInt(keyList.size());
		String server = keyList.get(randomPos);
		return server;
	}

}
