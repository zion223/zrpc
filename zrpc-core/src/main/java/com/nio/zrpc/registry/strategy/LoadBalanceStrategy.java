package com.nio.zrpc.registry.strategy;

import java.util.List;

public interface LoadBalanceStrategy {

    public String getInstance(List<String> map);

}
