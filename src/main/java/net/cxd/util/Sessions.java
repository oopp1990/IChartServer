package net.cxd.util;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.ApplicationContext;

public class Sessions {
	public static Map<Object, ChannelHandlerContext> sessions = new ConcurrentHashMap<Object, ChannelHandlerContext>();
	public static ApplicationContext applicationContext;
}
