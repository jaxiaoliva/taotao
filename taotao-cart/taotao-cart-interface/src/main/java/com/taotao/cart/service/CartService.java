package com.taotao.cart.service;

import java.util.List;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;

public interface CartService {
	/**
	 * 添加购物车
	 * 
	 * @param item
	 * @param num
	 * @param userId
	 * @return
	 */
	public TaotaoResult addItemCart(TbItem item, Integer num, Long userId);

	/**
	 * 根据用户ID和商品的ID查询是否存储在redis中
	 * 
	 * @param userId
	 * @param itemId
	 * @return null 说明不存在，如果不为空说明存在
	 */
	public TbItem queryTbItemByUserIdAndItemId(Long userId, Long itemId);

	/**
	 * 根据用户id查询商品列表（购物车）
	 * 
	 * @param userId
	 * @return
	 */
	public List<TbItem> queryCartListByUserId(Long userId);
}
