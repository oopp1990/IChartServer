package net.cxd.http.Service.impl;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.cxd.entity.FriendGroup;
import net.cxd.entity.User;
import net.cxd.entity.UserFriend;
import net.cxd.entity.UserInfo;
import net.cxd.http.Service.UserService;
import net.cxd.util.JedisUtil;
import net.cxd.util.Md5Encrypt;
import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cq.base.entity.BaseBean;
import cq.base.entity.ResultBean;

public class UserServiceImpl extends BaseServiceImpl implements UserService {

	@Override
	public void login(HttpServletRequest request, HttpServletResponse response) {
		Object obj = null;
		try {
			String name = (String) request.getParameter("name");
			String password = (String) request.getParameter("password");
			System.out.println(" login >>>>  name:" + name + " password : " + password);
			getBaseDao().setTableClass(User.class);
			List<BaseBean> list = getBaseDao().listBySql(" select * from im_user where name='"+name+"'");
			if (list != null && list.size() > 0) {
			User user =(User) list.get(0);
			
				if (user.getPassword().equals(Md5Encrypt.md5(password))) {
					obj = new ResultBean(JedisUtil.hget("userinfo",
							user.getId() + ""), true);
				} else {
					obj = new ResultBean("登录失败！请检查您的账号密码是否正确！", false);
				}
			} else {
				obj = new ResultBean("登录失败！无此帐号，请注册后登录！", false);
			}

		} catch (Exception e) {
			e.printStackTrace();
			obj = new ResultBean("登录出现未知异常，请与开发人员联系！", false);
		} finally {
			printToJson(response, obj);
		}
	}

	@Override
	public void changePassword(HttpServletRequest request,
			HttpServletResponse response) {
		Object msg = null;
		try {
			getBaseDao().setTableClass(User.class);
			Integer uid = Integer.parseInt(request.getParameter("uid"));
			String password = request.getParameter("password");
			User user = (User) get(uid);
			user.setPassword(Md5Encrypt.md5(password));
			msg = new ResultBean("修改密码成功！", update(user));//update
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultBean("出现未知异常， 请联系开发人员！！", false);
		} finally {
			printToJson(response, msg);
		}
	}

	@Override
	public void regist(HttpServletRequest request, HttpServletResponse response) {
		String name = (String) request.getParameter("name");
		String password = (String) request.getParameter("password");
		System.out.println(" regist >>>>  name:" + name + " password : " + password);
		Object msg = null;
		try {
			if (name.length() >= 6 && password.length() >= 6) {
				getBaseDao().setTableClass(User.class);
				User user = new User(name, Md5Encrypt.md5(password));
				add(user);
				UserInfo userInfo = new UserInfo();
				userInfo.setUid(user.getUid());
				userInfo.setName("昵称");
				userInfo.setLevel(1);
				userInfo.setVipLevel(0);
				getBaseDao().setTableClass(UserInfo.class);
				add(userInfo);
				JedisUtil.hset("userinfo", user.getUid() + "",
						userInfo.toString());
				msg = new ResultBean(userInfo.toString(), true);
			}else{
				msg = new ResultBean("帐号或者密码长度小于6", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultBean("出现未知异常， 请联系开发人员！！", false);
		} finally {
			printToJson(response, msg);
		}
	}

	@Override
	public void updateUserInfo(HttpServletRequest request,
			HttpServletResponse response) {
		Integer uid = Integer.parseInt(request.getParameter("uid"));
		String name = request.getParameter("name");
		String signature = request.getParameter("signature");
		String phoneNum = request.getParameter("phoneNum");
		String email = request.getParameter("email");
		String address = request.getParameter("address");
		String  photoFile = request.getParameter("photoFile");
		Jedis jedis = JedisUtil.getJedis();
		Object msg = null;
		try {
			String str = jedis.hget("userinfo", uid+"");
			if (str != null) {
				UserInfo  user = JSON.parseObject(str, UserInfo.class);
				if (name != null ) 
					user.setName(name);
				if (signature != null) 
					user.setSignature(signature);
				if (phoneNum != null ) 
					user.setPhoneNum(phoneNum);
				if (email != null) 
					user.setEmail(email);
				if (address != null)
					user.setAddress(address);
				if (photoFile != null) {
					user.setPhotoFile(photoFile);
				}
				getBaseDao().setTableClass(UserInfo.class);
				update(user);
				jedis.hset("userinfo", uid+"", user.toString());
				msg = new ResultBean("更新成功!", true);
			}else{
				msg = new ResultBean("查无此用户!", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			printToJson(response, msg);
			JedisUtil.getJedisPool().returnResource(jedis);
		}
	}

	public void printResultBean(HttpServletResponse response, boolean b,
			String msg) {
		if (response != null) {
			try {
				OutputStream out = response.getOutputStream();
				if (msg == null) {
					msg = b ? "OK" : "Fail";
				}
				ResultBean bean = new ResultBean(msg, b);
				out.write(bean.toString().getBytes("UTF-8"));
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void addFriendGroup(HttpServletRequest request,
			HttpServletResponse response) {
		FriendGroup group = new FriendGroup(Integer.parseInt(request
				.getParameter("uid")), request.getParameter("name"));
		Jedis jedis = JedisUtil.getJedis();
		Object msg = null;
		try {
			getBaseDao().setTableClass(FriendGroup.class);
			add(group);
			jedis.hset("FriendGroup", group.getUid() + "", group.toString());
			String str = jedis.hget("user:FriendGroup", group.getId() + "");
			List<FriendGroup> list = new ArrayList<FriendGroup>();
			if (str != null) {
				list = JSONArray.parseArray(str, FriendGroup.class);
			}
			list.add(group);
			jedis.hset("user:FriendGroup", group.getUid() + "",
					JSONArray.toJSONString(list));
			msg = new ResultBean("添加成功!", true);
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultBean("添加失败，出现未知异常，请联系开发人员！", false);
		} finally {
			JedisUtil.getJedisPool().returnResource(jedis);
			printToJson(response, msg);
		}
	}

	@Override
	public void deleteFriendGroup(HttpServletRequest request,
			HttpServletResponse response) {

		StringBuffer msg = new StringBuffer();
		boolean b = false;
		Jedis jedis = JedisUtil.getJedis();
		try {
			int uid = Integer.parseInt(request.getParameter("uid"));
			int gid = Integer.parseInt(request.getParameter("gid"));
			// 查看该组里面是否有
			getBaseDao().setTableClass(FriendGroup.class);
			String str = jedis.hget("user:Friend", gid + "");
			if (str != null) {
				msg.append("请先将该组用户全部移除！");
			} else {
				Integer[] ids = new Integer[] { gid };
				b = deletes(ids);
				jedis.hdel("FriendGroup", gid + "");
				String strs = jedis.hget("user:FriendGroup", uid + "");
				List<FriendGroup> list = new ArrayList<FriendGroup>();
				if (strs != null) {
					list = JSONArray.parseArray(str, FriendGroup.class);
					Iterator<FriendGroup> it = list.iterator();
					while (it.hasNext()) {
						FriendGroup g = it.next();
						if (g.getId() == gid) {
							it.remove();
							break;
						}
					}
					jedis.hset("user:FriendGroup", gid + "",
							JSONObject.toJSONString(list));
				}
				msg.append("删除成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg.append("出现未知异常 ，请联系开API开发人员！");
		} finally {
			JedisUtil.getJedisPool().returnResource(jedis);
//			printResultBean(response, b, msg.toString());
			printToJson(response, new ResultBean(msg.toString(), b));
		}
	}

	@Override
	public void addFriend(HttpServletRequest request,
			HttpServletResponse response) {
		Jedis jedis = JedisUtil.getJedis();
		StringBuffer msg = new StringBuffer();
		boolean b = false;
		try {
			UserFriend friend = new UserFriend(Integer.parseInt(request
					.getParameter("gid")), Integer.parseInt(request
					.getParameter("uid")));
			b = add(friend);

			jedis.hset("UserFriend", friend.getId() + "", friend.toString());
			String str = jedis.hget("user:Friend", friend.getGid() + "");
			List<UserFriend> list = new ArrayList<UserFriend>();
			if (str != null) {
				list = JSONArray.parseArray(str, UserFriend.class);
			}
			list.add(friend);
			jedis.hset("user:Friend", friend.getGid() + "",
					JSONArray.toJSONString(list));
			msg.append("更新成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg.append("更新出现未知错误，请联系开发人员!");
		} finally {
//			printResultBean(response, b, msg.toString());
			printToJson(response, new ResultBean(msg.toString(), b));
			JedisUtil.getJedisPool().returnResource(jedis);
		}
	}

	@Override
	public void removeFriendToOtherGroup(HttpServletRequest request,
			HttpServletResponse response) {
		// 获取该id的用户， 更新表 更新redis
		// 更新

		Jedis jedis = JedisUtil.getJedis();
		StringBuffer msg = new StringBuffer();
		boolean b = false;
		try {
			int id = Integer.parseInt(request.getParameter("id"));// 该记录id
			int gid = Integer.parseInt(request.getParameter("gid"));
			UserFriend friend = JSON.parseObject(
					jedis.hget("UserFriend", id + ""), UserFriend.class);
			friend.setGid(gid);
			getBaseDao().setTableClass(UserFriend.class);
			b = update(friend);// 更新数据库记录
			if (b) {
				jedis.hset("UserFriend", id + "", friend.toString());// 更新redis
				// 更新以前的redis记录 user:Friend
				String str = jedis.hget("user:Friend", friend.getGid() + "");
				List<UserFriend> list = new ArrayList<UserFriend>();
				if (str != null) {
					list = JSONArray.parseArray(str, UserFriend.class);
					Iterator<UserFriend> it = list.iterator();
					while (it.hasNext()) {
						UserFriend uf = it.next();
						if (uf.getUid() == friend.getUid()) {
							uf.setGid(gid);
							break;
						}
					}
				}
				jedis.hset("user:Friend", friend.getGid() + "",
						JSONArray.toJSONString(list));
			}
			msg.append("更新成功！");
		} catch (Exception e) {
			msg.append("更新出现未知错误， 请联系开发人员！");
			e.printStackTrace();
		} finally {
//			printResultBean(response, b, msg.toString());
			printToJson(response, new ResultBean(msg.toString(), b));
			JedisUtil.getJedisPool().returnResource(jedis);
		}
	}

	@Override
	public void removeFriend(HttpServletRequest request,
			HttpServletResponse response) {

		// 获取该id的用户， 更新表 更新redis
		// 更新

		Jedis jedis = JedisUtil.getJedis();
		StringBuffer msg = new StringBuffer();
		boolean b = false;
		try {
			int id = Integer.parseInt(request.getParameter("id"));// 该记录id
			int gid = Integer.parseInt(request.getParameter("gid"));
			UserFriend friend = JSON.parseObject(
					jedis.hget("UserFriend", id + ""), UserFriend.class);
			friend.setGid(gid);

			Integer[] ids = new Integer[] { id };
			getBaseDao().setTableClass(UserFriend.class);
			b = deletes(ids);// 更新数据库记录
			if (b) {
				jedis.hdel("UserFriend", id + "");// 更新redis
				// 更新以前的redis记录 user:Friend
				String str = jedis.hget("user:Friend", friend.getGid() + "");
				List<UserFriend> list = new ArrayList<UserFriend>();
				if (str != null) {
					list = JSONArray.parseArray(str, UserFriend.class);
					Iterator<UserFriend> it = list.iterator();
					while (it.hasNext()) {
						UserFriend uf = it.next();
						if (uf.getUid() == friend.getUid()) {
							it.remove();
							break;
						}
					}
				}
				jedis.hset("user:Friend", friend.getGid() + "",
						JSONArray.toJSONString(list));
			}
			msg.append("删除成功！");
		} catch (Exception e) {
			msg.append("删除出现未知错误， 请联系开发人员！");
			e.printStackTrace();
		} finally {
//			printResultBean(response, b, msg.toString());
			printToJson(response, new ResultBean(msg.toString(), b));
			JedisUtil.getJedisPool().returnResource(jedis);
		}
	}

	@Override
	public void getAllFriend(HttpServletRequest request,
			HttpServletResponse response) {
		String str = JedisUtil.hget("user:FriendGroup",
				request.getParameter("uid"));
		if (str != null) {
			List<FriendGroup> list = JSONArray.parseArray(str,
					FriendGroup.class);
			for (FriendGroup ff : list) {
				Jedis jedis = JedisUtil.getJedis();
				String strs = jedis.hget("user:Friend", ff.getUid() + "");
				if (strs != null) {
					ff.setFriends(JSONArray.parseArray(str, UserFriend.class));
				}
			}
			printToJson(response, list);
		}
	}

	public void printToJson(HttpServletResponse response, Object msg) {
		try {
			if (msg != null) {
				OutputStream out = response.getOutputStream();
				String json = JSONObject.toJSONString(msg);
				out.write(json.getBytes(CHAR_SET));
				out.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
