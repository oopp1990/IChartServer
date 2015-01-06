package net.cxd.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtil {
	private static JedisPool pool;
	static {
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxIdle(300);
			config.setTestOnBorrow(true);
			config.setTestOnReturn(true);
			pool = new JedisPool(config, "localhost", 6379);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static JedisPool getJedisPool() {
		return pool;
	}

	public static Jedis getJedis() {
		return pool.getResource();
	}

	public static void returnJedis(Jedis jedis) {
		pool.returnResource(jedis);
	}

	public long getScore() {
		return Long.parseLong((System.currentTimeMillis() + "").substring(1));
	}

	public static void hset(String key, String field, String value) {
		Jedis jedis = pool.getResource();
		jedis.hset(key, field, value);
		pool.returnResource(jedis);
	}

	public static void lpush() {

	}

	public static String hget(String key, String field) {
		Jedis jedis = pool.getResource();
		String str = jedis.hget(key, field);
		pool.returnResource(jedis);
		return str;
	}

}
