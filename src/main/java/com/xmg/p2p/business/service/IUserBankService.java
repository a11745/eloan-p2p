package com.xmg.p2p.business.service;

import com.xmg.p2p.business.domain.UserBankinfo;


/**
 * 绑定的银行卡相关服务
 *
 */
public interface IUserBankService {

	/**
	 * 获取用户绑定的银行信息
	 * @param id
	 * @return
	 */
	UserBankinfo getBankinfoByUser(Long id);

	/**
	 * 用户绑定银行卡
	 * @param userBankinfo
	 */
	void bindBank(UserBankinfo userBankinfo);

	
	
	
}
