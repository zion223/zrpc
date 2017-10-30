package com.nio.zrpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import com.alibaba.fastjson.JSONObject;
import com.nio.zrpc.definition.RpcDefinition;

public class Client {

	private static Channel channel;
	// protected static volatile Object result;
	//
	// protected static Object lock = new Object();
	volatile static Class returnType;

	public static void ZrpcClient(InetSocketAddress add) {

		ClientBootstrap clientBootstrap = new ClientBootstrap();
		ExecutorService boss = Executors.newCachedThreadPool();
		ExecutorService worker = Executors.newCachedThreadPool();
		clientBootstrap.setFactory(new NioClientSocketChannelFactory(boss,
				worker));
		clientBootstrap.setPipelineFactory(new ChannelPipelineFactory() {

			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addLast("decoder", new StringDecoder());
				pipeline.addLast("encoder", new StringEncoder());
				pipeline.addLast("hiHandler", new ClientHandler());
				return pipeline;
			}
		});
		ChannelFuture connect = clientBootstrap.connect(add);
		channel = connect.getChannel();
		System.out.println("client start");

	}

	@SuppressWarnings("unchecked")
	public static <T> T refer(final Class<T> interfaceName) {
		return (T) Proxy.newProxyInstance(Client.class.getClassLoader(),
				new Class[] { interfaceName }, new InvocationHandler() {

					public Object invoke(Object proxy, Method method,
							Object[] arguments) throws Throwable {
						// 转发请求 交给服务端执行
						// interface com.nio.service.HelloService
						String interfaceName1 = interfaceName.toString()
								.substring(10);
						returnType = method.getReturnType();
						RpcDefinition rpc = new RpcDefinition(interfaceName1,
								method.getName(), method.getParameterTypes(),
								arguments);
						String jsonString = JSONObject.toJSONString(rpc);
						ChannelFuture future = channel.write(jsonString);
						future.addListener(new ChannelFutureListener() {

							public void operationComplete(ChannelFuture future)
									throws Exception {
								// 消息发送完毕
								System.out.println("消息发送完毕");

							}

						});
						getResultThread getResultThread = new getResultThread();
						getResultThread.start();
						Thread.sleep(1000);
						return getResultThread.result;
					}
				});

	}


}

class getResultThread extends Thread {
	protected static volatile Object result;

	protected static Object lock = new Object();

	@Override
	public void run() {
		while (result == null) {
			synchronized (lock) {
				try {
					lock.wait();
					System.out.println("得到result" + result);
					if (Client.returnType == String.class) {
						System.out.println(Client.returnType);
						return;
					} else {
						
						System.out.println("返回类型:" + Client.returnType);
						System.out.println(Client.returnType);
						Object parseObject = JSONObject.parseObject(
								result.toString(), Client.returnType);
						System.out.println(parseObject);
						result = parseObject;
						System.out.println("get Thread:"
								+ Thread.currentThread().getName());
						return;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

}
