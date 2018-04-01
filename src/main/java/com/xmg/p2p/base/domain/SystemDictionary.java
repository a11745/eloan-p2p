package com.xmg.p2p.base.domain;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 数据字典分类
 * @author Administrator
 *
 */
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemDictionary extends BaseDomain {
	/**
	 * SystemDictionary：
		属性名称	属性类型	属性说明
		Sn	String	数据字典分类编码
		Title	String	数据字典分类名称
		Intro	String	数据字典分类使用说明
	 */
	private String sn;
	private String title;
	/**
	 * 返回当前的json字符串
	 */
	public String getJsonString(){
		Map<String, Object> json = new HashMap<>();
		json.put("id", id);
		json.put("sn", sn);
		json.put("title", title);
		return JSONObject.toJSONString(json);
	}
}
