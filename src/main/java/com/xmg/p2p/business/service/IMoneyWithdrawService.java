package com.xmg.p2p.business.service;

import java.math.BigDecimal;

import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.business.query.MoneyWithdrawQueryObject;

/**
 * 提现相关服务
 *
 */
public interface IMoneyWithdrawService {

	
	/**
	 * 用户提交提现申请
	 * @param moneyAmount
	 */
	void apply(BigDecimal moneyAmount);

	PageResult query(MoneyWithdrawQueryObject qo);

	/**
	 * 后台提现的审核
	 * @param id
	 * @param remark
	 * @param state
	 */
	void audit(Long id, String remark, int state);

	
	
}
