package net.cxd.server;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class TcpChannelInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		// TODO Auto-generated method stub
		ChannelPipeline pipeline = channel.pipeline();
		pipeline.addLast("hart", new IdleStateHandler(60, 15, 13,
				TimeUnit.SECONDS));
		pipeline.addLast("decoder", new StringDecoder(Charset.forName("UTF-8")));
		pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
		pipeline.addLast("encoder", new StringEncoder(Charset.forName("UTF-8")));
		// pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
		pipeline.addLast("tcpHandler", new TcpHandler());
	}

}
