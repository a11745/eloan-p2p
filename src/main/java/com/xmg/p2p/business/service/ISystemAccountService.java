package com.xmg.p2p.business.service;

import java.math.BigDecimal;

import com.xmg.p2p.business.domain.BidRequest;
import com.xmg.p2p.business.domain.MoneyWithdraw;
import com.xmg.p2p.business.domain.PaymentScheduleDetail;
import com.xmg.p2p.business.domain.SystemAccount;

/**
 * 平台账户的相关服务
 *
 */
public interface ISystemAccountService {

	
	void update(SystemAccount systemAccount) ;

	/**
	 * 创建平台的账户
	 */
	void initSystemAccount();

	/**
	 * 平台收取手续费
	 * @param borrowChargeFee
	 * @param bidRequest
	 */
	void chargeBorrowFee(BigDecimal borrowChargeFee, BidRequest bidRequest);
	
	/**
	 * 平台收取提现手续费
	 * @param m
	 */
	void moneyWithdrawChargeFee(MoneyWithdraw m);

	/**
	 * 平台收取利息管理费
	 * @param interestChargeFee
	 * @param psd
	 */
	void chargeInterestFee(BigDecimal interestChargeFee,PaymentScheduleDetail psd);
	
}
