package com.xmg.p2p.business.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.business.domain.BidRequest;
import com.xmg.p2p.business.domain.MoneyWithdraw;
import com.xmg.p2p.business.domain.PaymentScheduleDetail;
import com.xmg.p2p.business.domain.SystemAccount;
import com.xmg.p2p.business.domain.SystemAccountFlow;
import com.xmg.p2p.business.mapper.SystemAccountFlowMapper;
import com.xmg.p2p.business.mapper.SystemAccountMapper;
import com.xmg.p2p.business.service.ISystemAccountService;

@Service
public class SystemAccountServiceImpl implements ISystemAccountService {

	@Autowired
	private SystemAccountMapper systemAccountMapper;
	@Autowired
	private SystemAccountFlowMapper systemAccountFlowMapper ;
	
	public void update(SystemAccount systemAccount) {
		int ret  = this.systemAccountMapper.updateByPrimaryKey(systemAccount);
		if ( ret <= 0 ) {
			throw new RuntimeException("SystemAccount乐观锁失败");
		}
	}

	/**
	 * 创建一个系统平台的账户
	 */
	public void initSystemAccount() {
		 SystemAccount current = this.systemAccountMapper.selectCurrent();
		 if (current == null ) {
			current  = new SystemAccount();
			this.systemAccountMapper.insert(current);
		}
	}

	/**
	 * 平台收取手续费
	 */
	public void chargeBorrowFee(BigDecimal fee , BidRequest bidRequest) {
		//获取当前的系统账户
		SystemAccount current = this.systemAccountMapper.selectCurrent();
		//修改系统账户余额
		current.setUseableAmount(current.getUseableAmount().add(fee));
		//生成一条系统账户流水
		SystemAccountFlow flow = new SystemAccountFlow() ;
		flow.setAccountActionType(BidConst.SYSTEM_ACCOUNT_ACTIONTYPE_MANAGE_CHARGE);
		flow.setAccountId(current.getId());
		flow.setAmount(fee);
		flow.setVdate(new Date());
		flow.setUseableAmount(current.getUseableAmount()) ;
		flow.setFreezedAmount(current.getFreezedAmount());
		flow.setNote("借款"+ bidRequest.getTitle()+"成功,收取借款管理费" + fee );
		this.systemAccountFlowMapper.insert(flow);
		this.update(current);
	}
	/**
	 * 系统收取提现手续费
	 */
	@Override
	public void moneyWithdrawChargeFee(MoneyWithdraw m) {
		//获取当前的系统账户
		SystemAccount current = this.systemAccountMapper.selectCurrent();
		//修改系统账户余额
		current.setUseableAmount(current.getUseableAmount().add(m.getChargeFee()));
		//生成一条系统账户流水
		SystemAccountFlow flow = new SystemAccountFlow() ;
		flow.setAccountActionType(BidConst.SYSTEM_ACCOUNT_ACTIONTYPE_WITHDRAW_MANAGE_CHARGE);
		flow.setAccountId(current.getId());
		flow.setAmount(m.getChargeFee());
		flow.setVdate(new Date());
		flow.setUseableAmount(current.getUseableAmount()) ;
		flow.setFreezedAmount(current.getFreezedAmount());
		flow.setNote(m.getApplier().getUsername()+"提现"+m.getAmount()+"成功,收取提现手续费"+m.getChargeFee());
		this.systemAccountFlowMapper.insert(flow);
		this.update(current);
	}


	/**
	 * 平台收取利息管理费
	 */
	public void chargeInterestFee(BigDecimal interestChargeFee, PaymentScheduleDetail psd) {
		//获取当前的系统账户
		SystemAccount current = this.systemAccountMapper.selectCurrent();
		//修改系统账户余额
		current.setUseableAmount(current.getUseableAmount().add(interestChargeFee));
		//生成一条系统账户流水
		SystemAccountFlow flow = new SystemAccountFlow() ;
		flow.setAccountActionType(BidConst.SYSTEM_ACCOUNT_ACTIONTYPE_INTREST_MANAGE_CHARGE);
		flow.setAccountId(current.getId());
		flow.setAmount(interestChargeFee);
		flow.setVdate(new Date());
		flow.setUseableAmount(current.getUseableAmount()) ;
		flow.setFreezedAmount(current.getFreezedAmount());
		flow.setNote(psd.getToLogininfoId()+"收款"+psd.getTotalAmount()+"成功,收取利息管理费"+interestChargeFee);
		this.systemAccountFlowMapper.insert(flow);
		this.update(current);
	}
	
	
	
	
}
