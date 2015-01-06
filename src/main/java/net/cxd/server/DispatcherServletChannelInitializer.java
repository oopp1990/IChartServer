package net.cxd.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletException;

public class DispatcherServletChannelInitializer extends ChannelInitializer<SocketChannel> {

	private final DispatcherServlet dispatcherServlet;

	public DispatcherServletChannelInitializer() throws ServletException {

		MockServletContext servletContext = new MockServletContext();
		MockServletConfig servletConfig = new MockServletConfig(servletContext);

		AnnotationConfigWebApplicationContext wac = new AnnotationConfigWebApplicationContext();
		
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"application.xml");
		
		wac.setParent(context);
		wac.setServletContext(servletContext);
		wac.setServletConfig(servletConfig);
		wac.register(WebConfig.class);
		wac.refresh();

		this.dispatcherServlet = new DispatcherServlet(wac);
		this.dispatcherServlet.init(servletConfig);
	}

	@Override
	public void initChannel(SocketChannel channel) throws Exception {
		// Create a default pipeline implementation.
		ChannelPipeline pipeline = channel.pipeline();

		// Uncomment the following line if you want HTTPS
		//SSLEngine engine = SecureChatSslContextFactory.getServerContext().createSSLEngine();
		//engine.setUseClientMode(false);
		//pipeline.addLast("ssl", new SslHandler(engine));


		pipeline.addLast("decoder", new HttpRequestDecoder());
		pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
		pipeline.addLast("encoder", new HttpResponseEncoder());
		pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
		pipeline.addLast("handler", new ServletNettyHandler(this.dispatcherServlet));
	}


	@Configuration
	@EnableWebMvc
	@ComponentScan(basePackages="net.cxd.http.controll")
	static class WebConfig extends WebMvcConfigurerAdapter {
	}

}
