package com.xmg.p2p.business.query;

import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

import com.xmg.p2p.base.query.AuditQueryObject;

@Setter @Getter 
public class RechargeOfflineQueryObject extends AuditQueryObject {

	private Long applierId ; //申请人id  只能查看自己当前的充值记录
	
	private Long bankInfoId = -1L ; //  银行账户信息对应的id
	private String tradeCode ; //交易流水账号
	
	
	public String getTradeCode(){
		return StringUtils.hasLength(tradeCode) ? tradeCode : null ;
	}
	
}
