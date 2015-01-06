package net.cxd.server;

import net.cxd.entity.UserMsg;
import net.cxd.util.Sessions;
import cq.base.dao.BaseDao;
import cq.base.entity.BaseBean;

public class MessageService {
	private BaseDao<BaseBean> baseDao;
	private UserMsg msg;
	public MessageService() {
		if (Sessions.applicationContext != null) {
			baseDao =  Sessions.applicationContext
					.getBean("baseDao",BaseDao.class);
		}
	}
	
	
	
	
	

}
