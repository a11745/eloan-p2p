package com.xmg.p2p.business.domain;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.springframework.format.annotation.DateTimeFormat;

import com.xmg.p2p.base.domain.BaseDomain;

/**
 * 平台账户的流水
 * 
 * 
 */
@Setter
@Getter
public class SystemAccountFlow extends BaseDomain {

	private Long accountId; // 对应的平台账户
	private BigDecimal amount; // 流水金额
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date vdate; // 业务时间
	private int accountActionType; // 对应的 资金变化的类型
	private String note;
	private BigDecimal useableAmount; // 流水之后账户的 可用金额
	private BigDecimal freezedAmount; // 流水之后账户的 冻结金额

}
