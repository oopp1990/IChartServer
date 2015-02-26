package net.cxd.http.controll;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.cxd.http.Service.BaseService;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

@Controller
public class UserControll {
	@Resource(name = "userService")
	private BaseService baseService;

	public BaseService getBaseService() {
		return baseService;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	// @PathVariable String name, @PathVariable String password
	@RequestMapping(value = "/login")
	@ResponseBody
	public String login(MockHttpServletRequest request, MockHttpServletResponse response, HttpSession session) throws IllegalStateException, IOException, ServletException {
		getBaseService().login(request, response);
		return null;
	}

	@RequestMapping(value = "/regist")
	public String regist(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		getBaseService().regist(request, response);
		return null;
	}

	@RequestMapping(value = "/changePassword")
	public String changePassword(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		System.out.println(getBaseService());
		getBaseService().changePassword(request, response);
		return null;
	}

	@RequestMapping(value = "/updateUserInfo")
	public String updateUserInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		System.out.println(getBaseService());
		getBaseService().changePassword(request, response);
		return null;
	}

	@RequestMapping(value = "/addFriendGroup")
	public String addFriendGroup(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		getBaseService().addFriendGroup(request, response);
		return null;
	}

	@RequestMapping(value = "/deleteFriendGroup")
	public String deleteFriendGroup(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		getBaseService().deleteFriendGroup(request, response);
		return null;
	}

	@RequestMapping(value = "/getAllFriend")
	public String getAllFriend(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		getBaseService().getAllFriend(request, response);
		return null;
	}

	@RequestMapping(value = "/addFriend")
	public String addFriend(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		getBaseService().addFriend(request, response);
		return null;
	}

	@RequestMapping(value = "/removeFriend")
	public String removeFriend(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		getBaseService().removeFriend(request, response);
		return null;
	}

	@RequestMapping(value = "/moveFriendToOtherGroup")
	public String moveFriendToOtherGroup(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		getBaseService().removeFriendToOtherGroup(request, response);
		return null;
	}

}
