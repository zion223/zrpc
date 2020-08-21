package com.nio.zrpc.registry.strategy;

import java.util.List;
import java.util.Random;

public class RandomStrategy implements LoadBalanceStrategy {

	@Override
	public String  getInstance(List<String> keyList) {

		Random random = new Random();
		int randomPos = random.nextInt(keyList.size());
        return keyList.get(randomPos);
	}

}
