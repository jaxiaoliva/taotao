package com.taotao.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.cart.service.CartService;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.CookieUtils;
import com.taotao.common.util.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.service.ItemService;
import com.taotao.sso.service.UserLoginService;

@Controller
public class CartController {
	@Autowired
	private CartService cartService;
	@Autowired
	private UserLoginService loginService;
	@Autowired
	private ItemService itemService;

	@Value("${TT_TOKEN_KEY}")
	private String TT_TOKEN_KEY;
	@Value("${TT_CART_KEY}")
	private String TT_CART_KEY;

	@RequestMapping(value = "/cart/add/${itemid}")
	public String addCart(@PathVariable Long itemId, Integer num, HttpServletRequest request,
			HttpServletResponse response) {
		// 1.引入服务
		// 2.注入服务
		// 3.判断用户是否登录
		// 从cookie中获取用户的token信息
		String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
		// 调用SSO的服务查询用户的信息
		TaotaoResult result = loginService.getUserByToken(token);
		// 获取商品的数据
		TbItem tbItem = itemService.getItemById(itemId);
		if (result != null && result.getStatus() == 200) {
			// 4.如果已登录，调用service的方法
			TbUser user = (TbUser) result.getData();
			cartService.addItemCart(tbItem, num, user.getId());
		} else {
			// 5.如果没有登录 调用设置到cookie的方法
			// 先根据cookie获取购物车的列表
			List<TbItem> cartList = getCookieCartList(request);
			boolean flag = false;
			// 判断如果购物车中有包含要添加的商品 商品数量相加
			for (TbItem tbItem2 : cartList) {
				if (tbItem2.getId() == itemId.longValue()) {
					// 找到列表中的商品 更新数量
					tbItem2.setNum(tbItem2.getNum() + num);
					flag = true;
					break;
				}
			}
			if (flag) {
				// 如果找到对应的商品，更新数量后，还需要设置回cookie中
				CookieUtils.setCookie(request, response, TT_CART_KEY, JsonUtils.objectToJson(cartList), 7 * 24 * 3600,
						true);
			} else {
				// 如果没有就直接添加到购物车
				// 调用商品服务
				// 设置数量
				tbItem.setNum(num);
				// 设置图片为一张
				if (tbItem.getImage() != null) {
					tbItem.setImage(tbItem.getImage().split(",")[0]);
				}
				// 添加商品到购物车中
				cartList.add(tbItem);
				// 设置到cookie中
				CookieUtils.setCookie(request, response, TT_CART_KEY, JsonUtils.objectToJson(cartList), 7 * 24 * 3600);
			}
		}
		return "cartSuccess";
	}

	// -------------------------------------完美分割线------------------------------------------------------------
	// 获取购物车的列表
	public List<TbItem> getCookieCartList(HttpServletRequest request) {
		// 从cookie中获取商品的列表
		String jsonStr = CookieUtils.getCookieValue(request, TT_CART_KEY, true);// 商品的列表的JSON
		if (StringUtils.isNotBlank(jsonStr)) {
			List<TbItem> list = JsonUtils.jsonToList(jsonStr, TbItem.class);
			return list;
		}
		return new ArrayList<>();
	}
}
