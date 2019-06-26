package com.taotao.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.alibaba.druid.filter.AutoLoad;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.JsonUtils;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.jedis.JedisClient;
import com.taotao.sso.service.UserLoginService;

@Service
public class UserLoginServiceImpl implements UserLoginService {

	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private JedisClient client;
	@Value("${USER_INFO}")
	private String USER_INFO;
	@Value("${EXPIRE_TIME}")
	private Integer EXPIRE_TIME;

	@Override
	public TaotaoResult login(String username, String password) {
		System.out.println("name:" + username + ",password:" + password);
		// 1.注入mapper
		// 2.校验用户名密码是否为空
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			return TaotaoResult.build(400, "用户名或密码错误");
		}
		// 3.先校验用户名，再校验密码
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		// select * from tbuser where username=123
		List<TbUser> list = userMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			return TaotaoResult.build(400, "用户不存在");
		}
		TbUser user = list.get(0);
		// 先加密再比较
		String md5Pwd = DigestUtils.md5DigestAsHex(password.getBytes());
		if (!md5Pwd.equals(user.getPassword())) {
			return TaotaoResult.build(400, "密码错误");
		}
		// 4.如果校验成功
		// 5.生成token:uuid ,还需设置token有效期来模拟session
		// 用户的数据存放在redis(key:token,value:用户的json数据)
		String token = UUID.randomUUID().toString();
		// 存放用户数据到redis中，为了管理方便加一个前缀
		// 设置密码为空
		user.setPassword(null);
		client.set(USER_INFO + ":" + token, JsonUtils.objectToJson(user));
		client.expire(USER_INFO + ":" + token, EXPIRE_TIME);// 设置过期时间
		// 6.把token设置到cookie中 在表现层设置
		return TaotaoResult.ok(token);
	}

	@Override
	public TaotaoResult getUserByToken(String token) {
		// 1.注入jedisclient
		// 2.调用根据token查询用户信息的方法
		String userInfo = client.get(USER_INFO + ":" + token);
		// 3.判断是否能够查询到
		if (StringUtils.isNotBlank(userInfo)) {
			TbUser user = JsonUtils.jsonToPojo(userInfo, TbUser.class);
			// 重置过期时间
			client.expire(USER_INFO + ":" + token, EXPIRE_TIME);// 设置过期时间
			return TaotaoResult.ok(user);
		}
		return TaotaoResult.build(400, "用户信息已过期");
	}

	@Override
	public TaotaoResult logout(String token) {
		if (StringUtils.isEmpty(token)) {
			return TaotaoResult.build(400, "未登录");
		}
		client.expire(USER_INFO + ":" + token, 0);
		return TaotaoResult.build(200, "退出成功");
	}

}
