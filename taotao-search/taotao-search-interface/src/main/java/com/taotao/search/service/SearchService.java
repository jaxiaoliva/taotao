package com.taotao.search.service;

import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;

public interface SearchService {
	// 导入所有的商品数据到索引库中
	public TaotaoResult importAllSearchItems();

	// 根据搜索的条件搜索结果
	/**
	 * 
	 * @param queryStr 查询的主条件
	 * @param page 查询的当前页码
	 * @param rows 每页显示的行数 这个在controller中写死
	 * @return
	 */
	public SearchResult search(String queryStr, Integer page, Integer rows);
	
	/**
	 * 更新索引库
	 * @param itemId
	 * @return
	 */
	public TaotaoResult updateSearchItemById(Long itemId);
}
