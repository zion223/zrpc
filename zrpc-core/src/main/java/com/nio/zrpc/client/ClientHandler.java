package com.nio.zrpc.client;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nio.zrpc.definition.RpcResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class ClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
	
	private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);

	@Override
	protected void channelRead0(ChannelHandlerContext context, RpcResponse response) throws Exception {
		getResultThread.result=response.getResult();
		//通知去result线程
		synchronized (getResultThread.lock) {
			log.info("收到response"+response.getResult().getClass());
			getResultThread.lock.notify();
		}
		
	}

}
