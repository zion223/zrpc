package com.nio.zrpc.server;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nio.zrpc.definition.RpcRequest;
import com.nio.zrpc.definition.RpcResponse;
import com.nio.zrpc.hystrix.RpcHystrixCommand;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<RpcRequest>{
	
	private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);

	@Override
	protected void channelRead0(ChannelHandlerContext context, RpcRequest request) throws Exception {
		
		RpcResponse response = new RpcResponse();
		
		RpcHystrixCommand rpcHystrixCommand = new RpcHystrixCommand(request);
		
		Object result = rpcHystrixCommand.execute();
		response.setRequestId("requestId");
		response.setResult(result);
		context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}


	

	
}
