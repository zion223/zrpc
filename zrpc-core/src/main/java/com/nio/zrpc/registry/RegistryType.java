package com.nio.zrpc.registry;

public enum RegistryType {


    //注册类型
    ZOOKEEPER("Zookeeper", 0x01),
    CONSUL("Consul", 0x02);

    public static final int ZOOKEEPER_ID = 0X01;
    public static final int CONSULE_ID = 0X02;

    private final String key;
    private final int value;

    RegistryType(String key, int value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }
}
