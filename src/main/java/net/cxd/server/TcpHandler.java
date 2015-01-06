package net.cxd.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class TcpHandler extends SimpleChannelInboundHandler<String> {
	private static final String START_CHAR = "{";
	private static final String END_CHAR = "}";
	private static final String HART = "hart";
	private static final String ERROR = "error";

	@Override
	protected void channelRead0(ChannelHandlerContext channel, String msg)
			throws Exception {
		System.out.println(msg);
		msg = msg.trim();
		if (HART.equals(msg)) {
			System.out.println("心跳");
			return;
		}
		// TODO 业务逻辑
		if (msg.startsWith(START_CHAR) && msg.endsWith(END_CHAR)) {
			
		} else {
			channel.writeAndFlush(ERROR);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		if (ctx.channel().isActive()) {
			ctx.writeAndFlush(ERROR);
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {

		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state().equals(IdleState.READER_IDLE)) {
				System.out.println("READER_IDLE");
				// 超时关闭channel
				ctx.close();
			} else if (event.state().equals(IdleState.WRITER_IDLE)) {
				System.out.println("WRITER_IDLE");
			} else if (event.state().equals(IdleState.ALL_IDLE)) {
				System.out.println("ALL_IDLE");
				// 发送心跳
				ctx.channel().write(HART);
				ctx.channel().flush();
			}
		}
		super.userEventTriggered(ctx, evt);
	}
}
