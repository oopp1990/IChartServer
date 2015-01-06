package net.cxd.http.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface BaseService {
	public static final String CHAR_SET = "UTF-8";
	void login(HttpServletRequest request, HttpServletResponse response);
	void regist(HttpServletRequest request, HttpServletResponse response);
	void updateUserInfo(HttpServletRequest request, HttpServletResponse response);
	void changePassword(HttpServletRequest request, HttpServletResponse response);
	void addGroup(HttpServletRequest request, HttpServletResponse response);
	void getAllFriend(HttpServletRequest request, HttpServletResponse response);
	void addFriendGroup(HttpServletRequest request, HttpServletResponse response);
	void addFriend(HttpServletRequest request, HttpServletResponse response);
	void deleteFriendGroup(HttpServletRequest request,
			HttpServletResponse response);
	void removeFriendToOtherGroup(HttpServletRequest request,
			HttpServletResponse response);
	void removeFriend(HttpServletRequest request,
			HttpServletResponse response);
	
	
	
}
