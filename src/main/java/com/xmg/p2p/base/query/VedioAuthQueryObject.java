package com.xmg.p2p.base.query;

import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * 视频审核查询对象
 * @author Administrator
 *
 */
@Getter
@Setter
public class VedioAuthQueryObject extends AuditQueryObject {
	private String keyword ;
	
	public String getKeyword(){
		return StringUtils.hasLength(keyword) ? keyword : null; 
	}
	
}
