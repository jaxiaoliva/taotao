package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

/**
 * 用户注册接口
 * 
 * @author xiaolong
 *
 */
public interface UserRegisterService {
	/**
	 * 参数校验
	 * @param param 
	 * @param type 1:username 2:phone 3:email
	 * @return
	 */
	public TaotaoResult checkData(String param, Integer type);
	
	public TaotaoResult register(TbUser user);
}
