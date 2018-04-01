package com.xmg.p2p.business.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.business.domain.AccountFlow;
import com.xmg.p2p.business.domain.Bid;
import com.xmg.p2p.business.domain.BidRequest;
import com.xmg.p2p.business.domain.MoneyWithdraw;
import com.xmg.p2p.business.domain.PaymentSchedule;
import com.xmg.p2p.business.domain.PaymentScheduleDetail;
import com.xmg.p2p.business.domain.RechargeOffline;
import com.xmg.p2p.business.mapper.AccountFlowMapper;
import com.xmg.p2p.business.service.IAccountFlowService;
//import com.xmg.p2p.business.service.IAccountFlowServiceImpl;

/**
 * 账户流水相关服务
 * @author Linv999
 *
 */
@Service
public class AccountFlowServiceImpl  implements IAccountFlowService{

	@Autowired
	private AccountFlowMapper accountFlowMapper ;
	
	/**
	 * 添加一条线下充值单流水信息
	 * @param account
	 * @param ro
	 */
	//
	public void rechargeFlow(Account account, RechargeOffline ro) {
		AccountFlow flow = new AccountFlow(); //创建一个流水对象
		flow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_RECHARGE_OFFLINE);  //设置此次对应的流水类型 : 线下充值
		flow.setAccountId(account.getId()) ; // 设置当前流水对用的账号信息
		flow.setAmount(ro.getAmount()); //流水金额
		flow.setVdate(new Date());  //产生流水的业务时间
		flow.setUseableAmount(account.getUsableAmount()); // 已经在账户中更新过了
		flow.setFreezedAmount(account.getFreezedAmount());// 已经在账户中更新过了
		flow.setNote("线下充值成功, 充值金额为:" + ro.getAmount());
		this.accountFlowMapper.insert(flow);
	}
	
	/**
	 * 添加一条投标的流水
	 * @param account
	 * @param bid
	 */ //
	public void bidFlow(Account account, Bid bid) {
		AccountFlow flow = new AccountFlow(); //创建一个流水对象
		flow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_BID_FREEZED);  //设置此次对应的流水类型 :投标冻结金额
		flow.setAccountId(account.getId()) ; // 设置当前流水对用的账号信息
		flow.setAmount(bid.getAvailableAmount()); //流水金额
		flow.setVdate(new Date());  //产生流水的业务时间
		flow.setUseableAmount(account.getUsableAmount()); // 已经在账户中更新过了
		flow.setFreezedAmount(account.getFreezedAmount());// 已经在账户中更新过了
		flow.setNote("投标" +bid.getBidRequestTitle()+"的借款,投标冻结金额:"+bid.getAvailableAmount());
		this.accountFlowMapper.insert(flow);
	}

	/**
	 * 取消投标的流水
	 * @param bidAccount
	 * @param bid
	 */ //
	public void cancelBidFlow(Account account, Bid bid) {
		AccountFlow flow = baseFolw(account);
		flow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_BID_UNFREEZED);
		flow.setAmount(bid.getAvailableAmount()); //设置本次流水金额
		flow.setNote("投标" +bid.getBidRequestTitle()+"的借款,借款失败,取消冻结金额:"+bid.getAvailableAmount());
		this.accountFlowMapper.insert(flow);
	}
	
	/**
	 * 基本的流水信息
	 * @param account
	 * @return
	 */
	private AccountFlow baseFolw(Account account) {
		AccountFlow flow = new AccountFlow(); //创建一个流水对象
		flow.setVdate(new Date());  //产生流水的业务时间
		flow.setUseableAmount(account.getUsableAmount()); // 已经在账户中更新过了
		flow.setFreezedAmount(account.getFreezedAmount());// 已经在账户中更新过了
		flow.setAccountId(account.getId()) ; // 设置当前流水对用的账号信息
		return flow;
	}

	/**
	 * 成功借款的流水
	 * @param bidAccount
	 * @param bidRequest
	 */ //
	public void borrowSuccessFlow(Account account, BidRequest bidRequest) {
		AccountFlow flow = baseFolw(account);
		flow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_BIDREQUEST_SUCCESSFUL); //成功借款
		flow.setAmount(bidRequest.getBidRequestAmount());
		flow.setNote("借款" +bidRequest.getTitle()+"成功,收到借款金额:"+bidRequest.getBidRequestAmount());
		this.accountFlowMapper.insert(flow);
	}

	/**
	 * 生成的借款手续费的流水
	 * @param borrowChargeFee
	 * @param borrowAccount
	 * @param bidRequest
	 */ //
	public void borrowChargeFeeFlow(BigDecimal borrowChargeFee, Account account, BidRequest bidRequest) {
		AccountFlow flow = baseFolw(account);
		flow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_BIDREQUEST_SUCCESSFUL); 
		flow.setAmount(bidRequest.getBidRequestAmount());
		flow.setNote("借款" +bidRequest.getTitle()+"成功,支付借款手续费金额:"+borrowChargeFee);
		this.accountFlowMapper.insert(flow);
	}

	/**
	 * 借款成功之后对应的投标者生成的流水信息
	 * @param bidAccount
	 * @param bid
	 */  //
	public void bidSuccessFlow(Account account, Bid bid) {
		AccountFlow flow = baseFolw(account) ;
		flow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_BID_SUCCESSFUL); 
		flow.setAmount(bid.getAvailableAmount());
		flow.setNote("投标" +bid.getBidRequestTitle()+"成功,取消投标冻结金额:"+bid.getAvailableAmount());
		this.accountFlowMapper.insert(flow);
	}

	@Override
	public void whitdrawAppllyFlow(Account account, MoneyWithdraw m) {
		AccountFlow flow = baseFolw(account) ;
		flow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_WITHDRAW_FREEZED); 
		flow.setAmount(m.getAmount());
		flow.setNote("提现申请 ,冻结金额:"+m.getAmount());
		this.accountFlowMapper.insert(flow);
		
	}

	@Override
	public void moneyWithdrawSuccessFlow(Account account, MoneyWithdraw m) {
		AccountFlow flow = baseFolw(account) ;
		flow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_WITHDRAW); 
		flow.setAmount(m.getAmount().subtract(m.getChargeFee()));
		flow.setNote("提现成功,取消冻结金额:"+flow.getAmount());
		this.accountFlowMapper.insert(flow);
	}

	@Override
	public void moneyWithdrawChargeFeeFlow(Account account, MoneyWithdraw m) {
		AccountFlow flow = baseFolw(account) ;
		flow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_WITHDRAW_MANAGE_CHARGE); 
		flow.setAmount(m.getChargeFee());
		flow.setNote("提现成功,支付提现手续费:"+m.getChargeFee());
		this.accountFlowMapper.insert(flow);
	}

	@Override
	public void cancleWithdrawFlow(Account account, MoneyWithdraw m) {
		AccountFlow flow = baseFolw(account) ;
		flow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_WITHDRAW_UNFREEZED); 
		flow.setAmount(m.getAmount());
		flow.setNote(m.getApplier().getUsername()+"提现"+m.getAmount()+"失败,取消冻结金额"+m.getAmount());
		this.accountFlowMapper.insert(flow);
	}

	/**
	 * 生成成功还款的流水
	 */
	public void returnMoneyFlow(Account account, PaymentSchedule ps) {
		AccountFlow flow = baseFolw(account) ;
		flow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_RETURN_MONEY); 
		flow.setAmount(ps.getTotalAmount());
		flow.setNote("借款" +ps.getBidRequestTitle()+"第"+ps.getMonthIndex()+"期成功还款!");
		this.accountFlowMapper.insert(flow);
	}

	/**
	 * 生成收款人收款流水
	 */
	public void receiveMoneyFlow(Account account, PaymentScheduleDetail psd) {
		AccountFlow flow = baseFolw(account) ;
		flow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_CALLBACK_MONEY); 
		flow.setAmount(psd.getTotalAmount());
		flow.setNote("借款第"+psd.getMonthIndex()+"期成功收款!");
		this.accountFlowMapper.insert(flow);
	}

	/**
	 * 投资人收款成功,支付利息管理费
	 */
	@Override
	public void interestChargeFeeFlow(Account account,BigDecimal interestChargeFee, PaymentScheduleDetail psd) {
		AccountFlow flow = baseFolw(account) ;
		flow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_INTEREST_SHARE); 
		flow.setAmount(interestChargeFee);
		flow.setNote("借款第"+psd.getMonthIndex()+"期成功收款!支付利息管理费"+interestChargeFee);
		this.accountFlowMapper.insert(flow);
	}
}
