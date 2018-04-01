package com.xmg.p2p.business.service;

import java.math.BigDecimal;

import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.business.domain.Bid;
import com.xmg.p2p.business.domain.BidRequest;
import com.xmg.p2p.business.domain.MoneyWithdraw;
import com.xmg.p2p.business.domain.PaymentSchedule;
import com.xmg.p2p.business.domain.PaymentScheduleDetail;
import com.xmg.p2p.business.domain.RechargeOffline;

public interface IAccountFlowService {
	
	/**
	 * 添加一条线下充值单流水信息
	 * @param account
	 * @param ro
	 */
	public void rechargeFlow(Account account, RechargeOffline ro);

	/**
	 * 添加一条投标的流水
	 * @param account
	 * @param bid
	 */
	public void bidFlow(Account account, Bid bid);

	/**
	 * 取消投标的流水
	 * @param bidAccount
	 * @param bid
	 */
	public void cancelBidFlow(Account bidAccount, Bid bid);

	/**
	 * 基本的流水信息
	 * @param account
	 * @return
	 */
	public void borrowSuccessFlow(Account borrowAccount, BidRequest bidRequest);

	/**
	 * 成功借款的流水
	 * @param bidAccount
	 * @param bidRequest
	 */
	public void borrowChargeFeeFlow(BigDecimal borrowChargeFee, Account borrowAccount,
			BidRequest bidRequest);

	/**
	 * 借款成功之后对应的投标者生成的流水信息
	 * @param bidAccount
	 * @param bid
	 */
	public void bidSuccessFlow(Account bidAccount, Bid bid);
	
	
	/**
	 *  申请提现生成的流水
	 * @param account
	 * @param m
	 */
	public void whitdrawAppllyFlow(Account account, MoneyWithdraw m);

	/**
	 * 生成提现成功的流水
	 * @param account
	 * @param m
	 */
	public void moneyWithdrawSuccessFlow(Account account, MoneyWithdraw m);

	/**
	 * 生成支付提现手续费流水
	 * @param account
	 * @param m
	 */
	public void moneyWithdrawChargeFeeFlow(Account account, MoneyWithdraw m);

	/**
	 * 生成取消或拒绝提现的流水
	 * @param account
	 * @param m
	 */
	public void cancleWithdrawFlow(Account account, MoneyWithdraw m);

	/**
	 * 生成成功还款流水
	 * @param returnAccount
	 * @param ps
	 */
	public void returnMoneyFlow(Account returnAccount, PaymentSchedule ps);

	/**
	 * 生成收款收到的本息流水
	 * @param bidAccount
	 * @param psd
	 */
	public void receiveMoneyFlow(Account bidAccount, PaymentScheduleDetail psd);

	/**
	 * 投资人收款成功,支付利息管理费
	 * @param bidAccount
	 * @param interestChargeFee
	 * @param psd
	 */
	public void interestChargeFeeFlow(Account bidAccount,
			BigDecimal interestChargeFee, PaymentScheduleDetail psd);


}
