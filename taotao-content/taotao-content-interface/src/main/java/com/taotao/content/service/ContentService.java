package com.taotao.content.service;

import java.util.List;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

/**
 * 内容处理的接口
 * @author xiaolong
 *
 */
public interface ContentService {
	/**
	 * 插入内容表
	 */
	public TaotaoResult saveContent(TbContent content);
	
	/**
	 * 根据内容分类的ID，查询其下的内容列表
	 * @param categoryId
	 * @return
	 */
	public List<TbContent> getContentListByCategoryId(Long categoryId);
}
