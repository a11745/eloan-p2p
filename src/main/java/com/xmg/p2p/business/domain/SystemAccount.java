package com.xmg.p2p.business.domain;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

import com.xmg.p2p.base.domain.BaseDomain;
import com.xmg.p2p.base.util.BidConst;

/**
 * 平台的账户
 *
 */
@Setter @Getter 
public class SystemAccount extends BaseDomain {
	

	private int version ;  //版本号控制乐观锁
	private BigDecimal useableAmount = BidConst.ZERO ; //平台账户的可用金额
	private BigDecimal freezedAmount = BidConst.ZERO ;  //平台账户的冻结金额
	

}
