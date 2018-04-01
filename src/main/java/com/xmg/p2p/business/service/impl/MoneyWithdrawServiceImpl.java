package com.xmg.p2p.business.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.base.domain.RealAuth;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.base.util.BitStatesUtils;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.business.domain.MoneyWithdraw;
import com.xmg.p2p.business.domain.UserBankinfo;
import com.xmg.p2p.business.mapper.MoneyWithdrawMapper;
import com.xmg.p2p.business.query.MoneyWithdrawQueryObject;
import com.xmg.p2p.business.service.IAccountFlowService;

import com.xmg.p2p.business.service.IMoneyWithdrawService;
import com.xmg.p2p.business.service.ISystemAccountService;
import com.xmg.p2p.business.service.IUserBankService;

/**
 * 提现相关服务
 * @author Linv999
 *
 */
@Service
public class MoneyWithdrawServiceImpl implements IMoneyWithdrawService {
	
	@Autowired 
	private IUserinfoService userinfoService;
	@Autowired
	private IAccountService accountService;
	@Autowired
	private IUserBankService userBankService;
	@Autowired
	private MoneyWithdrawMapper moneyWithdrawMapper;
	@Autowired
	private  IAccountFlowService accountFlowService ;
	@Autowired
	private  ISystemAccountService systemAccountService; 
	
	
	/**
	 * 用户提交提现申请
	 */
	@Override
	public void apply(BigDecimal moneyAmount) {
		//得到当前用户
		Userinfo current = this.userinfoService.get(UserContext.getCurrent().getId());
		Account account = this.accountService.get(UserContext.getCurrent().getId());
		//判断用户没有提现申请在流程中 并且已经绑定银行卡
		if (!current.getHasWithdrawInProcess() 
				&& current.getIsBindBank()
				&& moneyAmount.compareTo(account.getUsableAmount()) <= 0  //提现金额小于账户可用余额
						//提现金额大于系统最小的提现
				&& moneyAmount.compareTo(BidConst.MIN_WITHDRAW_AMOUNT) >= 0  ) {
			 //得当当前用户绑定的银行卡的信息
			UserBankinfo ub = this.userBankService.getBankinfoByUser(current.getId());
			//创建一个提现申请对象 ,设置相关属性值
			MoneyWithdraw  m = new MoneyWithdraw();
			m.setAccountName(ub.getAccountName());
			m.setAccountNumber(ub.getAccountNumber());
			m.setAmount(moneyAmount);
			m.setApplier(UserContext.getCurrent());
			m.setApplyTime(new Date());
			m.setBankName(ub.getBankName());
			m.setChargeFee(BidConst.MONEY_WITHDRAW_CHARGEFEE);
			m.setForkName(ub.getForkName());
			m.setState(MoneyWithdraw.STATE_NORMAL); //待审核状态
			this.moneyWithdrawMapper.insert(m);
			//用户的可用余额减少,冻结金额增加
			account.setUsableAmount(account.getUsableAmount().subtract(moneyAmount));
			account.setFreezedAmount(account.getFreezedAmount().add(moneyAmount));
			this.accountFlowService.whitdrawAppllyFlow(account ,m) ;
			//给用户添加对应的状态码 
			current.addState(BitStatesUtils.OP_HAS_WITHDRAW_PROCESS) ;
			this.userinfoService.update(current) ;
			this.accountService.update(account);
		}
	
	}

	public PageResult query(MoneyWithdrawQueryObject qo) {
		int  count = this.moneyWithdrawMapper.queryForCount(qo);
		if (count > 0) {
			List<MoneyWithdraw> list = this.moneyWithdrawMapper.query(qo);
			return new PageResult(list, count, qo.getCurrentPage(), qo.getPageSize());
		}
		return PageResult.empty(qo.getPageSize());
	}

	
	
	public void audit(Long id, String remark, int state) {
		//得到提现申请, 判断状态 
		MoneyWithdraw m= this.moneyWithdrawMapper.selectByPrimaryKey(id);
		if (m != null && m.getState()== MoneyWithdraw.STATE_NORMAL) {
			//设置相关属性 
			m.setAuditor(UserContext.getCurrent());
			m.setAuditTime(new Date());
			m.setRemark(remark);
			m.setState(state);
			Userinfo userinfo = this.userinfoService.get(m.getApplier().getId());
			Account account = this.accountService.get(userinfo.getId());
			System.out.println("9899999999999999");
			System.out.println(this.accountService.get(m.getApplier().getId()));
			System.out.println("9899999get(m.getApplier().getId()999999999");
			System.out.println("9899999999999999");
			System.out.println(this.accountService.get(userinfo.getId()));
			System.out.println("989999999get(userinfo.getId()9999999");
			if (state == MoneyWithdraw.STATE_AUDIT) {
				// 审核通过
				// 减少冻结金额,生成一条提现流水
				account.setFreezedAmount(account.getFreezedAmount().subtract(m.getAmount().subtract(m.getChargeFee())));
				this.accountFlowService.moneyWithdrawSuccessFlow(account ,m );
				// 减少冻结金额,生成支付提现手续费流水
				account.setFreezedAmount(account.getFreezedAmount().subtract(m.getChargeFee()));
				this.accountFlowService.moneyWithdrawChargeFeeFlow(account ,m ) ;
				 //系统账户收到提现手续费,生成对应流水
				this.systemAccountService.moneyWithdrawChargeFee(m) ;
			} else {
				// 审核失败
				// 减少冻结金额,增加可用余额, 生成提现取消流水
				account.setFreezedAmount(account.getFreezedAmount().subtract(m.getAmount()));
				account.setUsableAmount(account.getUsableAmount().add(m.getAmount())) ;
				this.accountFlowService.cancleWithdrawFlow(account,m);
			}
			// 取消状态码
			userinfo.removeState(BitStatesUtils.OP_HAS_WITHDRAW_PROCESS);
			this.userinfoService.update(userinfo);
			this.accountService.update(account);
			this.moneyWithdrawMapper.updateByPrimaryKey(m);
		}
	}
	
	
	
}
