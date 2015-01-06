package net.cxd.server;

import javax.servlet.ServletException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class IChartServer {

	public void startHttp(int PORT) throws ServletException,
			InterruptedException {
		ServerBootstrap server = new ServerBootstrap();
		NioEventLoopGroup group = new NioEventLoopGroup();
		try {
			server.group(new NioEventLoopGroup(), new NioEventLoopGroup())
					.channel(NioServerSocketChannel.class).localAddress(PORT)
					.childHandler(new DispatcherServletChannelInitializer());
			server.bind().sync().channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}

	public void startTcp(int PORT) throws Exception {

		ServerBootstrap server = new ServerBootstrap();
		NioEventLoopGroup group = new NioEventLoopGroup();
		try {
			server.group(new NioEventLoopGroup(), new NioEventLoopGroup())
					.channel(NioServerSocketChannel.class).localAddress(PORT)
					.childHandler(new TcpChannelInitializer());
			server.bind().sync().channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}

	public static void startAll() {
		// TODO init spring
		final IChartServer chartServer = new IChartServer();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					System.out.println(" start yo listening  80");
					chartServer.startHttp(80);
				} catch (ServletException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					System.out.println(" start yo listening  8001");
					chartServer.startTcp(8001);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	public static void main(String[] args) {
		startAll();
//		try {
//			new IChartServer().startHttp(80);
//		} catch (ServletException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
