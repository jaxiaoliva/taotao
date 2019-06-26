package com.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;

/**
 * 内容分类
 * 
 * @author xiaolong
 *
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper mapper;

	@Override
	public List<EasyUITreeNode> getContentCategoryList(Long parentId) {
		// 1.注入mapper
		// 2.创建example
		TbContentCategoryExample example = new TbContentCategoryExample();
		// 3.设置条件
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);// select * from tbcontentcategory
												// where parent_id=1
		// 4.执行查询
		List<TbContentCategory> list = mapper.selectByExample(example);
		// 5.转化成EasyUITreeNode列表
		List<EasyUITreeNode> nodes = new ArrayList<>();
		for (TbContentCategory tbContentCategory : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setState(tbContentCategory.getIsParent() ? "closed" : "open");
			node.setText(tbContentCategory.getName());// 分类名称
			nodes.add(node);
		}
		// 6.返回
		return nodes;
	}

	@Override
	public TaotaoResult createContentCategory(Long parentId, String name) {
		// 1.构建对象 补全其他属性
		TbContentCategory category = new TbContentCategory();
		category.setCreated(new Date());
		category.setIsParent(false);// 新增节点为叶子节点
		category.setName(name);
		category.setParentId(parentId);
		category.setSortOrder(1);
		category.setStatus(1);
		category.setUpdated(category.getCreated());
		// 2.插入contentcategory表数据
		mapper.insertSelective(category);
		// 3.返回taotaoresult 包含内容分类的id 需要主键返回

		// 判断如果添加的节点的父节点本身为叶子节点，需要更新其为父节点
		TbContentCategory parent = mapper.selectByPrimaryKey(parentId);
		if (parent.getIsParent() == false) {// 原本为叶子节点
			parent.setIsParent(true);
			mapper.updateByPrimaryKey(parent);// 更新节点的is_parent属性为true
		}
		return TaotaoResult.ok(category);
	}

}
