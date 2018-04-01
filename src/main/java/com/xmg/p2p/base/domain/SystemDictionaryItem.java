package com.xmg.p2p.base.domain;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.Setter;
/**
 * 数据字典明细
 * @author Administrator
 *
 */
@Getter
@Setter
public class SystemDictionaryItem extends BaseDomain{
	/**
	 * SystemDictionaryItem：
		属性名称	属性类型	属性说明
		parentId	Long	数据字典明细对应的分类id
		Title	String	数据字典明细显示名称
		Intro	String	数据字典明细使用说明
		Tvalue	String	数据字典明细可选值
		Sequence	Integer	数据字典明细在该分类中的排序
	 */
	private Long parentId;
	private String title;
	private int sequence;
	/**
	 * 返回当前的json字符串
	 */
	public String getJsonString(){
		Map<String, Object> json = new HashMap<>();
		json.put("id", id);
		json.put("parentId", parentId);
		json.put("title", title);
		json.put("sequence", sequence);
		return JSONObject.toJSONString(json);
	}
}
