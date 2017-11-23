package com.nio.zrpc.client;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ClientHandler extends SimpleChannelHandler {
	private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		String s =  (String) e.getMessage();
		
		log.info("服务调用成功，返回的结果是:"+s);
		
//		User parseObject = JSONObject.parseObject(s, User.class);
		getResultThread.result=s;
		//通知去result线程
		synchronized (getResultThread.lock) {
			getResultThread.lock.notify();
		}
		//super.messageReceived(ctx, e);
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		log.info("channelConnected");
		super.channelConnected(ctx, e);
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		log.info("channelDisconnected");
		super.channelDisconnected(ctx, e);
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		log.info("channelClosed");
		super.channelClosed(ctx, e);
	}

}
