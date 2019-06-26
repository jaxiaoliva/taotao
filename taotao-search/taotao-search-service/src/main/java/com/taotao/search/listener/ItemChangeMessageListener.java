package com.taotao.search.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.service.SearchService;

/**
 * 接收消息的监听器
 * 
 * @author xiaolong
 *
 */
public class ItemChangeMessageListener implements MessageListener {
	// 注入service 直接调用方法更新即可
	@Autowired
	private SearchService service;

	@Override
	public void onMessage(Message message) {
		// 判断消息的类型是否为textmessage
		if (message instanceof TextMessage) {
			// 如果是获取商品的id
			TextMessage message2 = (TextMessage) message;
			String itemidStr;
			try {
				itemidStr = message2.getText();
				Long itemId = Long.parseLong(itemidStr);
				// 通过商品id查询数据 需要开发mapper 通过id查询商品(搜索时)的数据
				// 更新索引库
				TaotaoResult taotaoResult = service.updateSearchItemById(itemId);
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
