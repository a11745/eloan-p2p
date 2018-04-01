package com.xmg.p2p.business.query;

import lombok.Getter;
import lombok.Setter;

import com.xmg.p2p.base.query.AuditQueryObject;

/**
 * 还款计划查询对象
 *
 */
@Setter @Getter 
public class PaymentScheduleQueryObject extends AuditQueryObject {

	private Long logininfoId   = -1L ;
	private  Long bidRequestId ;
	
}
