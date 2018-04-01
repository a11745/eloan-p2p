package com.xmg.p2p.base.domain;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.xmg.p2p.base.util.BidConst;

import lombok.Getter;
import lombok.Setter;

/**
 * 封孔材料
 * @author Administrator
 *
 */
@Getter
@Setter
public class UserFile extends BaseAuditDomain {
	private String image;//风控材料图片
	private SystemDictionaryItem fileType;//风控材料分类
	private int score;//风控材料评分
	
	public String getJsonString(){
		Map<String,Object> json = new HashMap<>();
		json.put("id", id);
		json.put("applier", this.applier.getUsername());
		json.put("fileType", this.fileType.getTitle());
		json.put("image", BidConst.WEBSITE+image);
		return JSONObject.toJSONString(json);
	}
}
