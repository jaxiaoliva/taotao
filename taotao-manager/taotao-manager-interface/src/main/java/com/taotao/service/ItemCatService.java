package com.taotao.service;

import java.util.List;

import com.taotao.common.pojo.EasyUITreeNode;

/**
 * 商品分类的service接口
 * 
 * @author xiaolong
 *
 */
public interface ItemCatService {
	/**
	 * 根据父节点的id查询子节点列表
	 */
	public List<EasyUITreeNode> getItemCatListByParentId(Long parentId);
}
