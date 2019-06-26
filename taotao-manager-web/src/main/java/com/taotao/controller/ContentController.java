package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;
import com.taotao.service.ContentService;

@Controller
public class ContentController {
	@Autowired
	private ContentService service;

	@RequestMapping(value = "/content/save", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult saveContent(TbContent tbContent) {
		// 1.引入服务
		// 2.注入服务
		// 3.调用
		return service.saveContent(tbContent);
	}
}
