package com.taotao.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

/**
 * 内容处理的接口
 * 
 * @author xiaolong
 *
 */
public interface ContentService {
	public TaotaoResult saveContent(TbContent content);
}
