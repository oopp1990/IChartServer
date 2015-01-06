package net.cxd.http.Service.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.cxd.http.Service.BaseService;
import cq.base.dao.BaseDao;
import cq.base.entity.BaseBean;

public class BaseServiceImpl extends
		cq.base.service.impl.BaseServiceImpl<BaseBean> implements BaseService,
		cq.base.service.BaseService<BaseBean> {
	private BaseDao<BaseBean> baseDao;

	public BaseDao<BaseBean> getBaseDao() {
		return baseDao;// ? baseDao = new SSH2BaseDaoImpl<BaseBean>() : baseDao
	}

	public void setBaseDao(BaseDao<BaseBean> baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public void login(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void regist(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addGroup(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAllFriend(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addFriendGroup(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addFriend(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteFriendGroup(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeFriendToOtherGroup(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeFriend(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUserInfo(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changePassword(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}


}
