package net.cxd.server;

import javax.servlet.ServletException;

import net.cxd.util.Sessions;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class IChartServer {
	public void initSpring() {
		ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
		Sessions.app = context;
	}

	public void startHttp(int PORT) throws ServletException, InterruptedException {
		ServerBootstrap server = new ServerBootstrap();
		NioEventLoopGroup group = new NioEventLoopGroup();
		try {
			server.group(new NioEventLoopGroup(), new NioEventLoopGroup()).channel(NioServerSocketChannel.class).localAddress(PORT).childHandler(new DispatcherServletChannelInitializer1());
			server.bind().sync().channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}

	public void startTcp(int PORT) throws Exception {

		ServerBootstrap server = new ServerBootstrap();
		NioEventLoopGroup group = new NioEventLoopGroup();
		try {
			server.group(new NioEventLoopGroup(), new NioEventLoopGroup()).channel(NioServerSocketChannel.class).localAddress(PORT).childHandler(new TcpChannelInitializer());
			server.bind().sync().channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}

	public static void startAll(final int HTTP_PORT, final int TCP_PORT) {
		// TODO init spring
		final IChartServer chartServer = new IChartServer();
		chartServer.initSpring();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					System.out.println("start yo listenning " + HTTP_PORT);
					chartServer.startHttp(HTTP_PORT);
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
					System.out.println("start yo listening  " + TCP_PORT);
					chartServer.startTcp(TCP_PORT);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public static void main(String[] args) {

		startAll(80, 8080);
		// try {
		// new IChartServer().startHttp(80);
		// } catch (ServletException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}
}
