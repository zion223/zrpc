package com.nio.zrpc.server;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.nio.zrpc.core.InvokeService;
import com.nio.zrpc.definition.RpcDefinition;
import com.nio.zrpc.hystrix.RpcHystrixCommand;

public class ServerHandler extends SimpleChannelHandler{
	private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		log.info("channelClosed");
		super.channelClosed(ctx, e);
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent event)
			throws Exception {
		log.info("channelConnected"+event.getChannel().getRemoteAddress());
		//判断连接服务器  过滤
		//ctx.getChannel().close();
		super.channelConnected(ctx, event);
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		log.info("channelDisconnected");
		super.channelDisconnected(ctx, e);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		log.info("exceptionCaught");
		
		super.exceptionCaught(ctx, e);
	}

	@Override
	public void messageReceived(ChannelHandlerContext context, MessageEvent message)
			throws Exception {

		String buffer = (String) message.getMessage();
		
		log.info("received message:"+buffer);
		
		
		RpcDefinition rpc = JSONObject.parseObject(buffer,RpcDefinition.class);
		
		//调用service  使用hystrix进行调用
		//Object result1 = InvokeService.invokeService(rpc);
		RpcHystrixCommand rpcHystrixCommand = new RpcHystrixCommand(rpc);
		Object result1 = rpcHystrixCommand.execute();
		log.info(result1.toString());
		if(result1.getClass()==String.class){
			
			context.getChannel().write(result1);
		}else{
			String jsonString = JSONObject.toJSONString(result1);
			context.getChannel().write(jsonString);
		}
		
		super.messageReceived(context, message);
	}
	

	
}
