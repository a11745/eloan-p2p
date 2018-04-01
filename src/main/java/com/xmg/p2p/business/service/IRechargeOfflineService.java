package com.xmg.p2p.business.service;

import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.business.domain.RechargeOffline;
import com.xmg.p2p.business.query.RechargeOfflineQueryObject;


/**
 * 充值单相关服务
 *
 */
public interface IRechargeOfflineService {

	
	/**
	 * 前台页面申请一个充值单的流程
	 * @param ro
	 */
	void apply(RechargeOffline ro);

	PageResult query(RechargeOfflineQueryObject qo);


	void audit(Long id, String remark , int state);

	
	
}
