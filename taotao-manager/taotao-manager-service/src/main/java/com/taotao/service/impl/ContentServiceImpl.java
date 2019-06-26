package com.taotao.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.service.ContentService;

@Service
public class ContentServiceImpl implements ContentService {
	@Autowired
	private TbContentMapper mapper;

	@Override
	public TaotaoResult saveContent(TbContent content) {
		// 1.注入mapper

		// 2.补全其他属性
		content.setCreated(new Date());
		content.setUpdated(content.getCreated());
		// 3.插入内容表中
		mapper.insertSelective(content);
		return TaotaoResult.ok();
	}

}
