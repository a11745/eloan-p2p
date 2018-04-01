package com.xmg.p2p.base.query;

import org.springframework.util.StringUtils;

/**
 * 数据字典查询对象
 * @author Administrator
 *
 */
public class SystemDictionaryQuearyObject extends QueryObject {
	private String keyword;
	private Long parentId;
	public String getKeyword() {
		return StringUtils.hasLength(keyword)?keyword:null;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
}
