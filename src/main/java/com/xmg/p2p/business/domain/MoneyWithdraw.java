package com.xmg.p2p.business.domain;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import com.alibaba.fastjson.JSONObject;
import com.xmg.p2p.base.domain.BaseAuditDomain;


/**
 * 提现对象
 */
@Getter @Setter 
public class MoneyWithdraw extends BaseAuditDomain {

	private BigDecimal amount ;
	private BigDecimal chargeFee ;
	private String bankName ; //银行名称
	private String accountName ; //开户人姓名
	private String accountNumber  ; //开户人银行账号
	private String forkName ; //开户支行
	
	public String getJsonString (){
		Map<String , Object> json =  new HashMap<>();
		json.put("id", id) ;
		json.put("username", this.applier.getUsername()) ;
		json.put("realName", accountName) ;
		json.put("applyTime", applyTime) ;
		json.put("bankName", bankName) ;
		json.put("accountNumber", accountNumber) ;
		json.put("forkName", forkName) ;
		json.put("moneyAmount", amount) ;
		return JSONObject.toJSONString(json);
	}
	
	
	
}
