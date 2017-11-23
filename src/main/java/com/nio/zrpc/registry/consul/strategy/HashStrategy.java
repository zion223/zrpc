package com.nio.zrpc.registry.consul.strategy;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class HashStrategy implements LoadBalanceStrategy{

	@Override
	public String getInstance(Map<String, Integer> hashMap) {
		Set<String> keySet = hashMap.keySet();
		ArrayList<String> keyList = new ArrayList<String>();
		keyList.addAll(keySet);
		
		//int hashCode = hashMap.hashCode();
		Random random = new Random();
		int randomPos = random.nextInt(keyList.size());
		int hashCode=String.valueOf(randomPos).hashCode();
		int ServerListSize = keyList.size();
		int ServerPos=hashCode % ServerListSize;
		
		return keyList.get(ServerPos);
	}

}
