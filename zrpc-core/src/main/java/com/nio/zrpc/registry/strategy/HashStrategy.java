package com.nio.zrpc.registry.strategy;

import java.util.List;
import java.util.Random;

public class HashStrategy implements LoadBalanceStrategy {

    @Override
    public String getInstance(List<String> keyList) {

        Random random = new Random();
        int randomPos = random.nextInt(keyList.size());
        int hashCode = String.valueOf(randomPos).hashCode();
        int ServerListSize = keyList.size();
        int ServerPos = hashCode % ServerListSize;

        return keyList.get(ServerPos);
    }

}
