package net.cxd.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TcpHandler extends SimpleChannelInboundHandler<String> {
	private static final String START_CHAR="{";
	private static final String END_CHAR = "}";
	@Override
	protected void channelRead0(ChannelHandlerContext channel, String msg)
			throws Exception {
		System.out.println(msg);
		msg = msg.trim();
		if (msg.startsWith(START_CHAR) && msg.endsWith(END_CHAR)) {
			
		}else{
			
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		if (ctx.channel().isActive()) {
			//TODO  send error msg
		}
	}
	
}
