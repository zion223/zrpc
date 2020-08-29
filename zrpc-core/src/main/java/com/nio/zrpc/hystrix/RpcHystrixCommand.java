package com.nio.zrpc.hystrix;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.nio.zrpc.core.InvokeService;
import com.nio.zrpc.definition.RpcRequest;

public class RpcHystrixCommand extends HystrixCommand<Object> {
    private static final Logger log = LoggerFactory.getLogger(RpcHystrixCommand.class);

    private static final int DEFAULT_THREADPOOL_CORE_SIZE = 30;
    private final RpcRequest invoker;

    public RpcHystrixCommand(RpcRequest invoker) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(invoker.getMethodName()))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withCircuitBreakerRequestVolumeThreshold(20)//10秒钟内至少19此请求失败，熔断器才发挥起作用
                        .withCircuitBreakerSleepWindowInMilliseconds(30000)//熔断器中断请求30秒后会进入半打开状态,放部分流量过去重试
                        .withCircuitBreakerErrorThresholdPercentage(50)//错误率达到50开启熔断保护
                        .withExecutionTimeoutEnabled(false))//使用rpc的超时，禁用这里的超时
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(DEFAULT_THREADPOOL_CORE_SIZE)));//线程池为30

        this.invoker = invoker;
    }

    @Override
    protected Object run() throws Exception {
        //Object result = null;
        Object result = InvokeService.invoke(invoker);
        if (result == null) {
            throw new NullPointerException("调用服务失败");
        }

        return result;
    }

    @Override
    protected Object getFallback() {
        // 本地方法调用
        Object result = null;
        try {
            log.info(invoker.getDef().getFallbackClass());
            Class<?> clazz = Class.forName(invoker.getDef().getFallbackClass());
            Method method = clazz.getDeclaredMethod(invoker.getDef()
                    .getFallbackMethod());
            Object obj = clazz.newInstance();
            // TODO 服务失败调用的方法不可以传参数
            result = method.invoke(obj, null);
            log.info("==============fallback=========" + result);
            return result;
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("No fallback available.");
        }
    }

}
