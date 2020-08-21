package com.nio.zrpc.registry.consul.entity;

import java.util.ArrayList;
import java.util.List;

public class ServiceRegisterDefinition {

    private String id;
    private String name;
    private List<String> tag;
    private String address;
    private List<Integer> port;
    private String CheckIntervalTime = "5s";

    public String getCheckIntervalTime() {
        return CheckIntervalTime;
    }

    public void setCheckIntervalTime(String checkIntervalTime) {
        CheckIntervalTime = checkIntervalTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ServiceRegisterDefinition(String id, String name, List<String> tag,
                                     String address, List<Integer> port, String time) {
        super();
        this.id = id;
        this.name = name;
        this.tag = tag;
        this.address = address;
        this.port = port;
        this.CheckIntervalTime = time;
    }

    public ServiceRegisterDefinition(String id, String name, String tag, String address,
                                     String port, String time) {
        super();
        this.id = id;
        this.name = name;
        this.CheckIntervalTime = time;
        // TODO 暂时不支持tag
        List<String> arrayList = new ArrayList<>();
        arrayList.add("defaultTag");
        this.tag = arrayList;
        this.address = address;
        String[] splitport = port.split(" ");
        ArrayList<Integer> portlist = new ArrayList<Integer>();
        for (String sp : splitport) {
            portlist.add(Integer.parseInt(sp));
        }
        this.port = portlist;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }

    public String getAdress() {
        return address;
    }

    public void setAdress(String adress) {
        this.address = adress;
    }

    public List<Integer> getPort() {
        return port;
    }

    public void setPort(List<Integer> port) {
        this.port = port;
    }


}
