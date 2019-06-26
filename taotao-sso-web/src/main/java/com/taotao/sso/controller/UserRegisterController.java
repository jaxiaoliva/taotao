package com.taotao.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserRegisterService;

@Controller
public class UserRegisterController {

	@Autowired
	private UserRegisterService registerService;

	/**
	 * url /user/check/{param}/{type}
	 * 
	 * @param param
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/user/check/{param}/{type}", method = RequestMethod.GET)
	@ResponseBody
	public TaotaoResult checkData(@PathVariable String param, @PathVariable Integer type) {
		// 1.引入服务
		// 2.注入
		// 3.调用
		return registerService.checkData(param, type);
	}

	/**
	 * 注册 /user/register post username 用户名 password 密码 phone 手机号 email 邮箱
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult register(TbUser user) {
//		System.out.println("name:" + user.getUsername());
		return registerService.register(user);
	}
}
