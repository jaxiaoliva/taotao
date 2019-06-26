package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.service.UserRegisterService;

@Service
public class UserRegisterServiceImpl implements UserRegisterService {

	@Autowired
	private TbUserMapper userMapper;

	@Override
	public TaotaoResult checkData(String param, Integer type) {
		// 1.注入mapper
		// 2.根据参数动态生成查询的条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		if (type == 1) {// 用户名
			if (StringUtils.isEmpty(param)) {
				return TaotaoResult.ok(false);
			}
			criteria.andUsernameEqualTo(param);
		} else if (type == 2) {// 电话
			criteria.andPhoneEqualTo(param);
		} else if (type == 3) {// 邮箱
			criteria.andEmailEqualTo(param);
		} else {
			// 参数非法
			return TaotaoResult.build(400, "非法的参数");
		}
		// 3.获取调用mapper的查询方法 获取数据
		List<TbUser> list = userMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return TaotaoResult.ok(false);
		}
		// 4.如果查询到了数据--数据不可以用 false
		// 5.如果没有查询到数据--数据可用 true

		return TaotaoResult.ok(true);
	}

	@Override
	public TaotaoResult register(TbUser user) {
		// 1.注入mapper
		// 2.校验数据
		if (StringUtils.isEmpty(user.getUsername())) {
			return TaotaoResult.build(400, "注册失败，用户名不能为空");
		}
		if (StringUtils.isEmpty(user.getPassword())) {
			return TaotaoResult.build(400, "注册失败，密码不能为空");
		}
		// 校验用户名 电话及邮箱是否已注册
		TaotaoResult result = checkData(user.getUsername(), 1);
		if (!(boolean) result.getData()) {
			// 数据不可用
			return TaotaoResult.build(400, "用户名已被注册");
		}
		if (StringUtils.isNotBlank(user.getPhone())) {
			TaotaoResult phoneResult = checkData(user.getPhone(), 2);
			if (!(boolean) phoneResult.getData()) {
				return TaotaoResult.build(400, "电话号码已被注册");
			}
		}
		if (StringUtils.isNotBlank(user.getEmail())) {
			TaotaoResult emailResult = checkData(user.getEmail(), 3);
			if (!(boolean) emailResult.getData()) {
				return TaotaoResult.build(400, "邮箱已被注册");
			}
		}
		// 3.如果校验成功 补全其他属性
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());
		// 4.对密码进行md5加密
		String md5Pwd = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pwd);
		// 5.插入数据
		userMapper.insertSelective(user);
		// 返回
		return TaotaoResult.ok();
	}

}
