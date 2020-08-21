package com.nio.zrpc.registry.zookeeper.entity;

public class ZkServiceRegisterDefinition {

    private String interfaceName;
    private String url;
    private String port;
    private String strategy;

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public ZkServiceRegisterDefinition(String interfaceName, String port) {
        super();
        this.interfaceName = interfaceName;
        this.port = port;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

}
