package com.taotao.test.pagehelper;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;

public class TestPageHelper {
	@Test
	public void testHelper() {
		// 1.初始化spring容器
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		// 2.获取mapper的代理对象
		TbItemMapper mapper = context.getBean(TbItemMapper.class);
		// 3.设置分页信息
		PageHelper.startPage(1, 3);
		// 4.调用mapper的方法查询数据
		TbItemExample example = new TbItemExample();// 设置查询条件使用
		List<TbItem> list = mapper.selectByExample(example);
		List<TbItem> list2 = mapper.selectByExample(example);
		// 取分页信息
		PageInfo<TbItem> info = new PageInfo<TbItem>(list);

		System.out.println("第一个分页的list长度:" + list.size());
		System.out.println("第二个分页的list长度:" + list2.size());

		// 5.遍历结果集
		System.out.println("查询的总记录数:" + info.getTotal());
		for (TbItem tbItem : list) {
			System.out.println(tbItem.getId() + ">>" + tbItem.getTitle());
		}
	}
}
