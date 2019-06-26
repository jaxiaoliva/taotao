package com.taotao.portal.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.util.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.portal.service.UserService;
import com.taotao.portal.service.impl.UserServiceImpl;

public class LoginInterceptor implements HandlerInterceptor {

	// @Autowired
	// private UserServiceImpl userServiceImpl;

	// @Autowired
	// private UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("preHandle...");
		// 在Handler执行之前处理
		// 判断用户是否登录
		// 1.从cookie中获取token,使用token获取用户信息
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		// 2.如果获取不到用户信息，把用户请求的url作为参数传递给登录页面 返回false
		// TbUser user = userServiceImpl.getUserByToken(token);
		// TbUser user = userService.getUserByToken(token);
		// if (user == null) {
		// // response.sendRedirect(userServiceImpl.SSO_BASE_URL +
		// // userServiceImpl.SSO_PAGE_LOGIN + "?redirect="
		// // + request.getRequestURL());
		// response.sendRedirect("http://localhost:8088" + "/page/login" +
		// "?redirect=" + request.getRequestURL());
		// return false;
		// }
		// 3.获取到用户，返回true
		return true;
		// 返回值决定handler是否执行，true：执行，false:不执行
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// handler执行之后，返回ModelAndView之前

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 返回ModelAndView之后

	}

}
