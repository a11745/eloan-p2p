package com.xmg.p2p.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.base.domain.RealAuth;
import com.xmg.p2p.base.mapper.AccountMapper;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.business.domain.RechargeOffline;
import com.xmg.p2p.business.mapper.RechargeOfflineMapper;
import com.xmg.p2p.business.query.RechargeOfflineQueryObject;
import com.xmg.p2p.business.service.IRechargeOfflineService;


import com.xmg.p2p.business.service.IAccountFlowService;
//import com.xmg.p2p.business.service.IAccountFlowServiceImpl;


/**
 * 充值单相关服务
 * @author Linv999
 *
 */
@Service
public class RechargeOfflineServiceImpl implements IRechargeOfflineService {

	@Autowired
	private RechargeOfflineMapper rechargeOfflineMapper ;
	@Autowired
	private IAccountService accountService ;
	@Autowired 
	private IAccountFlowService accountFlowService ;
	
	
	
	/**
	 * 前台页面申请一个充值单的流程
	 */
	public void apply(RechargeOffline ro) {
		//一般做法是new一个对象  此处偷懒
		ro.setApplier(UserContext.getCurrent());
		ro.setApplyTime(new Date());
		ro.setState(RechargeOffline.STATE_NORMAL);
		this.rechargeOfflineMapper.insert(ro);
		
	}

	public PageResult query(RechargeOfflineQueryObject qo) {
		int  count = this.rechargeOfflineMapper.queryForCount(qo);
		if (count > 0) {
			List<RechargeOffline> list = this.rechargeOfflineMapper.query(qo);
			return new PageResult(list, count, qo.getCurrentPage(), qo.getPageSize());
		}
		return PageResult.empty(qo.getPageSize());
	}


	/**
	 * 后台审核线下充值单的流程
	 */
	public void audit(Long id, String remark, int state) {
		//查出线下充值单对象 ,并判断状态 
	RechargeOffline ro = this.rechargeOfflineMapper.selectByPrimaryKey(id);
	 if (ro != null  && ro.getState() == RechargeOffline.STATE_NORMAL) {
		//设置相关属性
		 ro.setAuditor(UserContext.getCurrent());
		 ro.setAuditTime(new Date());
		 ro.setRemark(remark);
		 ro.setState(state); 
		 //审核通过
		 if (state == RechargeOffline.STATE_AUDIT) {
			//得到申请人的账户信息  可用余额增加
			 Account account = this.accountService.get(ro.getApplier().getId());
			 account.setUsableAmount(account.getUsableAmount().add(ro.getAmount()));
			 //修改账户信息
			 this.accountService.update(account);
			 //添加一条流水信息
			 this.accountFlowService.rechargeFlow(account ,ro);
		}
		 this.rechargeOfflineMapper.updateByPrimaryKey(ro);
	}
	}
}
