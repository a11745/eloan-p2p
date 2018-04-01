package com.xmg.p2p.business.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

import com.xmg.p2p.base.domain.BaseDomain;

/**
 * 账户流水
 *
 */
@Setter @Getter
public class AccountFlow extends BaseDomain {

	private Long accountId ;  //对应的账户id
	private BigDecimal amount ; //金额 
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date vdate ; // 业务时间
	private int accountActionType ; //对应的 资金变化的类型
	private String note ;  
	private BigDecimal useableAmount ;  //流水之后账户的 可用金额
	private BigDecimal freezedAmount ; //流水之后账户的  冻结金额
	
}
