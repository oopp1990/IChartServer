package net.cxd.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.util.List;

import net.cxd.entity.UserGroup;
import net.cxd.entity.UserMsg;
import net.cxd.util.JedisUtil;
import net.cxd.util.MsgType;
import net.cxd.util.Sessions;
import net.cxd.util.TcpStatus;
import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import cq.base.dao.BaseDao;
import cq.base.entity.BaseBean;
import cq.base.service.BaseService;
import cq.base.service.impl.BaseServiceImpl;

public class MessageService extends BaseServiceImpl<BaseBean> implements
		BaseService<BaseBean> {

	private BaseDao<BaseBean> baseDao;
	private UserMsg msg;
	private final AttributeKey<String> counter = new AttributeKey<String>(
			"counter");

	public void read(ChannelHandlerContext channel, String strMsg) {
		if (strMsg.startsWith(TcpStatus.START_CHAR)
				&& strMsg.endsWith(TcpStatus.END_CHAR)) {
			Jedis jedis = JedisUtil.getJedis();
			try {
				msg = JSON.parseObject(strMsg, UserMsg.class);

				Integer oid;
				switch (msg.getMsgType()) {
				case MsgType.LOGIN:
					Attribute<String> attr = channel.attr(counter);
					String a = channel.attr(counter).get();
					a = msg.getUid() + "";
					attr.set(a);
					Sessions.sessions.put(a, channel);
					// TODO send she no send msg
					sendFirstMsg(a, channel, jedis);
					break;
				case MsgType.USERMSG:
					oid = msg.getOid();
					sendMessage(msg, oid, strMsg, jedis);
					break;
				case MsgType.GROUPMSG:
					Integer gid = msg.getOid();
					String str = jedis.hget("group", gid + "");
					List<UserGroup> list = JSONArray.parseArray(str,
							UserGroup.class);
					for (UserGroup ug : list) {
						oid = ug.getUid();
						if (oid == msg.getUid()) {
							continue;
						}
						sendMessage(msg, oid, strMsg, jedis);
					}
					break;
				default:
					break;
				}

			} catch (Exception e) {
				channel.writeAndFlush(TcpStatus.ERROR);
			} finally {
				JedisUtil.returnJedis(jedis);
			}
		} else {
			channel.writeAndFlush(TcpStatus.ERROR);
		}
	}

	public void sendFirstMsg(String uid, ChannelHandlerContext ctx, Jedis jedis) {
		while (true) {
			String str = jedis.lpop(("userNotReadMsg:" + uid));
			if (str != null) {
				ctx.write(str);
				ctx.flush();
			} else {
				return;
			}
		}
	}

	private void saveNotReadMsg(int oid, String buf, Jedis jedis) {
		jedis.lpushx(("userNotReadMsg:" + oid), buf);
		jedis.lpushx(("userRoamMsg:" + oid), buf);
	}

	private void sendMessage(BaseBean baseBean, int oid, String buf, Jedis jedis) {
		try {
			getBaseDao().setTableClass(baseBean.getClass());
			getBaseDao().add(baseBean);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		ChannelHandlerContext context = Sessions.sessions.get(oid + "");
		if (context != null) {
			try {
				context.write(buf);
				context.flush();
				jedis.lpushx(("userRoamMsg:" + oid), buf);
			} catch (Exception e) {
				saveNotReadMsg(oid, buf, jedis);
				Sessions.sessions.remove(oid + "");
				context.close();
				e.printStackTrace();
			}
		} else {
			saveNotReadMsg(oid, buf, jedis);
		}
	}
}
