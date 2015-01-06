package net.cxd.http.controll;

import javax.annotation.Resource;

import net.cxd.http.Service.BaseService;

import org.springframework.stereotype.Controller;

@Controller
public class FileControll {
	@Resource(name="userService")
	private BaseService baseService;
}
