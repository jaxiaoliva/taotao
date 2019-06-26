package com.taotao.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.mapper.SearchItemMapper;

/**
 * 从索引库搜索商品的dao
 * 
 * @author xiaolong
 *
 */
@Repository
public class SearchDao {

	@Autowired
	private SolrServer solrServer;

	@Autowired
	private SearchItemMapper mapper;

	/**
	 * 根据查询条件查询结果集
	 * 
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public SearchResult search(SolrQuery query) throws Exception {
		SearchResult searchResult = new SearchResult();
		// 1.创建solrserver对象 由spring管理注入
		// 2.直接执行查询
		QueryResponse response = solrServer.query(query);
		// 3.获取结果集
		SolrDocumentList results = response.getResults();
		// 设置searchResult的总记录数
		searchResult.setRecordCount(results.getNumFound());
		// 4.遍历结果集
		// 定义一个集合
		List<SearchItem> itemList = new ArrayList<>();

		// 取高亮
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

		for (SolrDocument solrDocument : results) {
			// 将solrdocument中的属性一个个的设置到searchItem中
			SearchItem item = new SearchItem();
			item.setCategory_name(solrDocument.get("item_category_name").toString());
			item.setId(Long.parseLong(solrDocument.get("id").toString()));
			item.setImage(solrDocument.get("item_image").toString());
			item.setPrice((Long) solrDocument.get("item_price"));
			item.setSell_point(solrDocument.get("item_sell_point").toString());
			// 取高亮
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			// 判断list是否为高亮
			String gaoliangStr = "";
			if (list != null && list.size() > 0) {
				// 有高亮
				gaoliangStr = list.get(0);
			} else {
				gaoliangStr = solrDocument.get("item_title").toString();
			}

			item.setTitle(gaoliangStr);
			// item.setItem_desc(solrDocument.get("item_desc").toString());
			// searchItem 封装到SearchResult中的itemList中
			itemList.add(item);
		}
		// 5.设置SearchResult的属性
		searchResult.setItemList(itemList);
		return searchResult;
	}

	// 更新索引库
	public TaotaoResult updateSearchItemById(Long itemId) throws Exception {
		// 注入mapper
		// 查询到记录
		SearchItem searchItem = mapper.getSearchItemById(itemId);
		// 把记录更新到索引库
		// 创建solrserver 注入进来
		// 创建solrinputdocument对象
		SolrInputDocument document = new SolrInputDocument();
		// 向文档对象中添加域
		document.addField("id", searchItem.getId().toString());
		document.addField("item_title", searchItem.getTitle());
		document.addField("item_sell_point", searchItem.getSell_point());
		document.addField("item_price", searchItem.getPrice());
		document.addField("item_image", searchItem.getImage());
		document.addField("item_category_name", searchItem.getCategory_name());
		document.addField("item_desc", searchItem.getItem_desc());
		// 向索引库添加文档
		solrServer.add(document);
		// 提交
		solrServer.commit();
		return TaotaoResult.ok();
	}
}
