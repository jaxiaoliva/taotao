package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.CookieUtils;
import com.taotao.common.util.JsonUtils;
import com.taotao.sso.service.UserLoginService;

@Controller
public class UserLoginController {

	@Autowired
	private UserLoginService loginService;
	@Value("${TT_TOKEN_KEY}")
	private String TT_TOKEN_KEY;

	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult login(HttpServletRequest request, HttpServletResponse response, String username,
			String password) {
		// 1.引入服务
		// 2.注入服务
		// 3.调用服务
		TaotaoResult result = loginService.login(username, password);
		// 4.需要设置token到cookie中，可以使用工具类，cookie需要跨域
		if (result != null && result.getStatus() == 200) {
			CookieUtils.setCookie(request, response, TT_TOKEN_KEY, result.getData().toString());
		}
		return result;
	}

	/**
	 * methon:get url:/user/token/{token}
	 * 
	 * @param token
	 * @return
	 */
	// @RequestMapping(value = "/user/token/{token}", method =
	// RequestMethod.GET)
	// @ResponseBody
	// public TaotaoResult getUserByToken(@PathVariable String token) {
	// TaotaoResult result = loginService.getUserByToken(token);
	// return result;
	// }

	@RequestMapping(value = "/user/token/{token}", produces = MediaType.APPLICATION_JSON_VALUE
			+ ";charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String getUserByToken(@PathVariable String token, String callback) {
		TaotaoResult result = loginService.getUserByToken(token);
		if (StringUtils.isNotBlank(callback)) {
			String strResult = callback + "(" + JsonUtils.objectToJson(result) + ");";
			return strResult;
		}
		return JsonUtils.objectToJson(result);
	}

	@RequestMapping(value = "/user/logout/{token}", method = RequestMethod.GET)
	@ResponseBody
	public TaotaoResult logout(@PathVariable String token) {
		TaotaoResult result = loginService.logout(token);
		return result;
	}
}
