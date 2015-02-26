package net.cxd.server;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.stream.ChunkedStream;
import io.netty.util.CharsetUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

public class ServletNettyHandler1 extends SimpleChannelInboundHandler<FullHttpRequest> {

	private final String tempPath = this.getClass().getClassLoader().getResource("").getPath() + "upload\\";
	private final HttpServlet servlet;

	private final ServletContext servletContext;

	public ServletNettyHandler1(HttpServlet servlet) {
		this.servlet = servlet;
		this.servletContext = servlet.getServletConfig().getServletContext();
	}

	private MockHttpServletRequest createServletRequest(FullHttpRequest fullHttpRequest) {

		UriComponents uriComponents = UriComponentsBuilder.fromUriString(fullHttpRequest.getUri()).build();

		MockHttpServletRequest servletRequest = new MockHttpServletRequest(this.servletContext);
		servletRequest.setCharacterEncoding("UTF-8");
		servletRequest.setRequestURI(uriComponents.getPath());
		servletRequest.setPathInfo(uriComponents.getPath());
		servletRequest.setMethod(fullHttpRequest.getMethod().name());
		// System.out.println(servletRequest.getMethod());
		if (uriComponents.getScheme() != null) {
			servletRequest.setScheme(uriComponents.getScheme());
		}
		if (uriComponents.getHost() != null) {
			servletRequest.setServerName(uriComponents.getHost());
		}
		if (uriComponents.getPort() != -1) {
			servletRequest.setServerPort(uriComponents.getPort());
		}

		for (String name : fullHttpRequest.headers().names()) {
			servletRequest.addHeader(name, fullHttpRequest.headers().get(name));
		}
		ByteBuf bbContent = fullHttpRequest.content();
		String contentType = servletRequest.getContentType();
		if (bbContent.isReadable()) {
			if (contentType != null && contentType.indexOf("form-data") > 0) {
				byte[] baContent = new byte[bbContent.readableBytes()];
				bbContent.readBytes(baContent);
				// System.out.println(new String(baContent));
				servletRequest.setContent(baContent);
				DiskFileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				try {
					// factory.
					List<FileItem> items = upload.parseRequest(servletRequest);// 上传文件解析
					Iterator<FileItem> itr = items.iterator();// 枚举方法
					while (itr.hasNext()) {
						FileItem item = itr.next();
						if (item.isFormField()) {
							String key = item.getFieldName();
							if (item.getName() != null) {
								writeFileToDisk(servletRequest, item, item.getName());
							} else {
								String value = item.getString("UTF-8");
								servletRequest.setParameter(key, value);
							}
						} else {
							if (item.getName() != null) {
								writeFileToDisk(servletRequest, item, item.getName());
							} else {
								String key = item.getFieldName();
								String value = item.getString("UTF-8");
								servletRequest.setParameter(key, value);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					String str = bbContent.toString(CharsetUtil.UTF_8);
					QueryStringDecoder queryStringDecoder = new QueryStringDecoder("/?" + str);
					Map<String, List<String>> params = queryStringDecoder.parameters();
					Iterator<String> it = params.keySet().iterator();
					while (it.hasNext()) {
						String key = it.next();
						List<String> pa = params.get(key);
						servletRequest.addParameter(key, pa != null && pa.size() > 0 ? pa.get(0) : null);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		try {
			if (uriComponents.getQuery() != null) {
				String query = UriUtils.decode(uriComponents.getQuery(), "UTF-8");
				servletRequest.setQueryString(query);
			}

			for (Entry<String, List<String>> entry : uriComponents.getQueryParams().entrySet()) {
				for (String value : entry.getValue()) {
					servletRequest.addParameter(UriUtils.decode(entry.getKey(), "UTF-8"), UriUtils.decode(value, "UTF-8"));
				}
			}
		} catch (UnsupportedEncodingException ex) {
			// shouldn't happen
		}

		return servletRequest;
	}

	public void writeFileToDisk(HttpServletRequest servletRequest, FileItem item, String name) throws Exception {
		// System.out.println("上传文件的大小:" + item.getSize());
		// System.out.println("上传文件的类型:" + item.getContentType());
		// // item.getName()返回上传文件在客户端的完整路径名称
		// System.out.println("上传文件的名称:" + name);
		// 此时文件暂存在服务器的内存当中
		// // 构造临时对象
		File tempFile = new File(tempPath);
		if (!tempFile.exists()) {
			tempFile.mkdir();
		}
		File temp = new File(tempPath, System.currentTimeMillis() + "");
		// 获取根目录对应的真实物理路径
		item.write(temp);// 保存文件在服务器的物理磁盘中
		File file = new File(tempFile, name);
		temp.renameTo(file);
//		temp.delete();
		servletRequest.setAttribute(name, file);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		if (ctx.channel().isActive()) {
			sendError(ctx, INTERNAL_SERVER_ERROR);
		}
	}

	private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		ByteBuf content = Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8);

		FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HTTP_1_1, status, content);
		fullHttpResponse.headers().add(CONTENT_TYPE, "text/plain; charset=UTF-8");

		// Close the connection as soon as the error message is sent.
		ctx.write(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
		if (!fullHttpRequest.getDecoderResult().isSuccess()) {
			sendError(channelHandlerContext, BAD_REQUEST);
			return;
		}

		MockHttpServletRequest servletRequest = createServletRequest(fullHttpRequest);
		MockHttpServletResponse servletResponse = new MockHttpServletResponse();

		this.servlet.service(servletRequest, servletResponse);

		HttpResponseStatus status = HttpResponseStatus.valueOf(servletResponse.getStatus());
		HttpResponse response = new DefaultHttpResponse(HTTP_1_1, status);

		for (String name : servletResponse.getHeaderNames()) {
			for (Object value : servletResponse.getHeaderValues(name)) {
				response.headers().add(name, value);
			}
		}

		channelHandlerContext.write(response);

		InputStream contentStream = new ByteArrayInputStream(servletResponse.getContentAsByteArray());

		// Write the content and flush it.
		ChannelFuture writeFuture = channelHandlerContext.writeAndFlush(new ChunkedStream(contentStream));
		writeFuture.addListener(ChannelFutureListener.CLOSE);

	}
}
