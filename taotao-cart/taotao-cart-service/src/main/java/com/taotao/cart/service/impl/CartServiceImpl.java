package com.taotao.cart.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.cart.jedis.JedisClient;
import com.taotao.cart.service.CartService;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.JsonUtils;
import com.taotao.pojo.TbItem;

@Service
public class CartServiceImpl implements CartService {

	@Value("${TT_CART_REDIS_PRE_KEY}")
	private String TT_CART_REDIS_PRE_KEY;

	@Autowired
	private JedisClient jedisClient;

	@Override
	public TaotaoResult addItemCart(TbItem item, Integer num, Long userId) {
		// 1.从redis数据库中查询该用户的购物车的商品的列表
		// 2.判断 如果 该商品在购物车的列表中，数量相加
		TbItem itemR = queryTbItemByUserIdAndItemId(userId, item.getId());
		if (itemR == null) {
			// 3.如果商品不存在，则 将商品直接添加到购物车
			// 将图片的内容设置成一张
			String image = item.getImage();
			if (StringUtils.isNotBlank(image)) {
				item.setImage(image.split(",")[0]);
			}
			// 设置购买数量
			item.setNum(num);
			// 将对象转换成POJO的JSON存储到redis中
			jedisClient.hset(TT_CART_REDIS_PRE_KEY + ":" + userId, item.getId() + "", JsonUtils.objectToJson(item));
		} else {
			// 4.如果商品存在，则获取商品的相关数据，然后数量相加
			itemR.setNum(itemR.getNum() + num);
			// 将购物车重新设置回去
			jedisClient.hset(TT_CART_REDIS_PRE_KEY + ":" + userId, item.getId() + "", JsonUtils.objectToJson(itemR));
		}
		return TaotaoResult.ok();
	}

	// 通过用户id和商品的id查询所对应的商品的数据，如果存在则不为空。
	@Override
	public TbItem queryTbItemByUserIdAndItemId(Long userId, Long itemId) {
		String str = jedisClient.hget(TT_CART_REDIS_PRE_KEY + ":" + userId, itemId + "");
		if (StringUtils.isNotBlank(str)) {
			TbItem tbItem = JsonUtils.jsonToPojo(str, TbItem.class);
			return tbItem;
		} else {
			return null;
		}
	}

	@Override
	public List<TbItem> queryCartListByUserId(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
