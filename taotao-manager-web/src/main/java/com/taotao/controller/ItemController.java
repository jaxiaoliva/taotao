package com.taotao.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import com.taotao.web.util.FastDFSClient;

@Controller
public class ItemController {
	@Value("${TAOTAO_IMAGE_SERVER_URL}")
	private String TAOTAO_IMAGE_SERVER_URL;

	@Autowired
	private ItemService itemService;

	// url:/item/list
	// method:get
	// 参数:page,rows
	// 返回值:json
	@RequestMapping(value = "item/list", method = RequestMethod.GET)
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		// 1.引入服务
		// 2.注入服务
		// 3.调用服务的方法
		return itemService.getItemList(page, rows);
	}

	@RequestMapping(value = "/pic/upload", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
	@ResponseBody
	public String uploadImage(MultipartFile uploadFile) {
		try {
			// 1.获取元文件的扩展名
			String originalFilename = uploadFile.getOriginalFilename();
			String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
			// 2.获取文件的字节数组
			byte[] bytes = uploadFile.getBytes();
			// 3.通过fastdfsclient的方法上传图片(参数要求：字节数组 扩展名 不包含".")
			FastDFSClient client = new FastDFSClient("classpath:resource/fastdfs.conf");
			// 返回值：group1/M00/00/00/wKgZhVk4vDqAaJ9jAA1rIuRd3Es177.jpg
			String str = client.uploadFile(bytes, extName);
			// 拼接成完整的URL
			String path = TAOTAO_IMAGE_SERVER_URL + str;
			// 4.成功时，设置map
			Map<String, Object> map = new HashMap();
			map.put("error", 0);
			map.put("url", path);
			return JsonUtils.objectToJson(map);
		} catch (Exception e) {
			// 4.失败时，设置map
			Map<String, Object> map = new HashMap();
			map.put("error", 1);
			map.put("message", "上传失败");
			return JsonUtils.objectToJson(map);
		}
	}

	/**
	 * 添加商品的方法
	 * 
	 * @param item
	 * @param desc
	 * @return
	 */
	@RequestMapping(value = "/item/save", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult saveItem(TbItem item, String desc) {
		// 1.引入服务
		// 2.注入服务
		// 3.调用
		return itemService.saveItem(item, desc);
	}
}
