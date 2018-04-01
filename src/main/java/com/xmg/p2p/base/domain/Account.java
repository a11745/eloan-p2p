package com.xmg.p2p.base.domain;

import java.math.BigDecimal;

import com.xmg.p2p.base.util.BidConst;

import lombok.Getter;
import lombok.Setter;
/**
 * 用户对应的账户对象
 * @author Administrator
 *
 */
@Getter
@Setter
public class Account extends BaseDomain {
	/**
	 * 用户账户信息对象：
		Account：
		属性名称	属性类型	属性说明
		version	Int	对象版本信息（用作乐观锁）
		tradePassword	String	交易密码
		usableAmount	BigDecimal	账户可用余额
		freezedAmount	BigDecimal	账户冻结金额
		unReceiveInterest	BigDecimal	账户待收利息
		unReceivePrincipal	BigDecimal	账户待收本金
		unReturnAmount	BigDecimal	账户待还金额
		remainBorrowLimit	BigDecimal	账户剩余授信额度
		borrowLimit	BigDecimal	账户授信额度
		注意：用户账户对象和登陆用户关联靠id，Account对象的id值==Logininfo对象的id值；
	 */
	private int version;
	private String tradePassword;
	private BigDecimal usableAmount = BidConst.ZERO;
	private BigDecimal freezedAmount = BidConst.ZERO;
	private BigDecimal unReceiveInterest = BidConst.ZERO;
	private BigDecimal unReceivePrincipal = BidConst.ZERO;
	private BigDecimal unReturnAmount = BidConst.ZERO;
	private BigDecimal remainBorrowLimit = BidConst.INIT_BORROW_LIMIT;
	private BigDecimal borrowLimit = BidConst.INIT_BORROW_LIMIT;
	
	public BigDecimal getTotalAmount(){
		return usableAmount.add(freezedAmount).add(unReceivePrincipal);
	}
}
