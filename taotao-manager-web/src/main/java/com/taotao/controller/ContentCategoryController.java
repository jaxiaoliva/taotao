package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;

@Controller
public class ContentCategoryController {
	@Autowired
	private ContentCategoryService service;

	/**
	 * url : '/content/category/list', animate: true, method : "GET", 参数:id
	 */
	@RequestMapping(value = "/content/category/list", method = RequestMethod.GET)
	@ResponseBody
	public List<EasyUITreeNode> getContentCategoryList(@RequestParam(value = "id", defaultValue = "0") Long parentId) {
		// 1.引入服务
		// 2.注入服务
		// 3.调用
		return service.getContentCategoryList(parentId);
	}

	/**
	 * url:/content/category/create method=post 参数:parentId,name
	 * 返回值:taotaoresult 包含分类id
	 */
	@RequestMapping(value = "/content/category/create", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult createContentCategory(Long parentId, String name) {
		return service.createContentCategory(parentId, name);
	}
}
