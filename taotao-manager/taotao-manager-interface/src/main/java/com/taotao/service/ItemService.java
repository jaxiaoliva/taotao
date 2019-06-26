package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

/**
 * 商品相关的service
 * 
 * @author xiaolong
 *
 */
public interface ItemService {
	/**
	 * 根据当前的页码和每页的行数进行分页查询
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	public EasyUIDataGridResult getItemList(Integer page, Integer rows);

	/**
	 * 添加商品基本数据和描述数据
	 * 
	 * @param item
	 * @param desc
	 * @return
	 */
	public TaotaoResult saveItem(TbItem item, String desc);

	/**
	 * 根据商品的id查询商品的数据
	 * @param itemId
	 * @return
	 */
	public TbItem getItemById(Long itemId);
	
	/**
	 * 根据商品id查询商品数据
	 * @param itemId
	 * @return
	 */
	public TbItemDesc getItemDescById(Long itemId);
}
