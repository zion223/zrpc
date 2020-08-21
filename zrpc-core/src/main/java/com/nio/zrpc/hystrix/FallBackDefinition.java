package com.nio.zrpc.hystrix;

import java.io.Serializable;

public class FallBackDefinition implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String fallbackClass;
    private String fallbackMethod;

    public String getFallbackClass() {
        return fallbackClass;
    }

    public void setFallbackClass(String fallbackClass) {
        this.fallbackClass = fallbackClass;
    }

    public String getFallbackMethod() {
        return fallbackMethod;
    }

    public FallBackDefinition() {
        super();
    }

    @Override
    public String toString() {
        return "FallBackDefinition [fallbackClass=" + fallbackClass
                + ", fallbackMethod=" + fallbackMethod + "]";
    }

    public void setFallbackMethod(String fallbackMethod) {
        this.fallbackMethod = fallbackMethod;
    }

    public FallBackDefinition(String fallbackClass, String fallbackMethod) {
        super();
        this.fallbackClass = fallbackClass;
        this.fallbackMethod = fallbackMethod;
    }

}
