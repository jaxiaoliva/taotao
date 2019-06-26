package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;

/**
 * 用户登录
 * 
 * @author xiaolong
 *
 */
public interface UserLoginService {
	/**
	 * 
	 * @param username
	 * @param pwd
	 * @return 登录成功 返回200 包含一个token数据 登录失败返回400
	 */
	public TaotaoResult login(String username, String password);

	/**
	 * 根据token来获取用户信息
	 * 
	 * @param token
	 * @return
	 */
	public TaotaoResult getUserByToken(String token);

	/**
	 * 退出
	 * 
	 * @param token
	 */
	public TaotaoResult logout(String token);
}
