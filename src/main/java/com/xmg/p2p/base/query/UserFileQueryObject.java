package com.xmg.p2p.base.query;

import com.xmg.p2p.base.domain.BaseAuditDomain;

import lombok.Getter;
import lombok.Setter;
/**
 * 用户风控文件审核查询
 * @author Administrator
 *
 */
@Getter
@Setter
public class UserFileQueryObject extends AuditQueryObject {
	private Long applierId;
	
}
