package com.taotao.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.JsonUtils;
import com.taotao.content.jedis.JedisClient;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;

@Service
public class CotentServiceImpl implements ContentService {
	@Value("${CONTENT_KEY}")
	private String CONTENT_KEY;

	@Autowired
	private JedisClient client;

	@Autowired
	private TbContentMapper mapper;

	@Override
	public List<TbContent> getContentListByCategoryId(Long categoryId) {
		// 添加缓存不能影响正常的业务逻辑

		// 判断是否redis中有数据，如果有，直接从redis中获取数据，返回
		// 从redis数据库中获取内容分类T的所有内容
		try {
			String jsonStr = client.hget(CONTENT_KEY, categoryId + "");
			// 如果存在，说明有缓存
			if (StringUtils.isNoneBlank(jsonStr)) {
				System.out.println("获取到缓存");
				return JsonUtils.jsonToList(jsonStr, TbContent.class);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 1.注入mapper
		// 2.创建example
		TbContentExample example = new TbContentExample();
		// select * from tbcontent where category id=1
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		// 3.设置查询的条件
		// 4.执行查询
		List<TbContent> list = mapper.selectByExample(example);
		// 5.返回

		// 将数据写入到redis数据库中
		// 注入jedisclient
		// 调用方法写入redis
		try {
			System.out.println("没有找到缓存");
			client.hset(CONTENT_KEY, categoryId + "", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public TaotaoResult saveContent(TbContent content) {
		// 注入mapper
		// 补全其他属性
		content.setCreated(new Date());
		content.setUpdated(content.getCreated());
		// 插入内容列表
		mapper.insertSelective(content);
		try {
			client.hdel(CONTENT_KEY, content.getCategoryId() + "");
			System.out.println("插入数据时，清空缓存");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.ok();
	}

}
