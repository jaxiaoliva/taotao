package com.taotao.item.pojo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.taotao.pojo.TbItem;

public class Item extends TbItem{
	
	public Item(TbItem item){
		BeanUtils.copyProperties(item, this);
	}
	
	public String[] getImages(){
		if(StringUtils.isNotBlank(getImage())){
			return getImage().split(",");
		}
		return null;
	}  
}
