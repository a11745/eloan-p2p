package com.xmg.p2p.business.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

import com.alibaba.fastjson.JSONObject;
import com.xmg.p2p.base.domain.BaseAuditDomain;

/**
 * 线下充值单
 *
 */

@Setter @Getter
public class RechargeOffline extends BaseAuditDomain{

	private PlatformBankinfo bankInfo ;   //银行信息对象
	private String 	tradeCode ; //交易号
	private Date tradeTime ; //交易时间
	private BigDecimal amount ;//  交易金额
	private String note ; //交易说明
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	public void setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
	}
	
	public String getJsonString(){
		Map<String, Object> json  = new HashMap<>();
		json.put("id", id);
		json.put("username", this.getApplier().getUsername());
		json.put("tradeCode", tradeCode);
		json.put("tradeTime", tradeTime);
		json.put("amount", amount);
		json.put("note", note);
		
		return JSONObject.toJSONString(json);
	}
	
	
	
}
