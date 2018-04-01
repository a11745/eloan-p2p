package com.xmg.p2p.business.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.BitStatesUtils;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.business.domain.UserBankinfo;
import com.xmg.p2p.business.mapper.UserBankinfoMapper;
import com.xmg.p2p.business.service.IUserBankService;

/**
 * 前台绑定银行卡相关服务
 * @author Linv999
 *
 */
@Service
public class UserBankServiceImpl implements IUserBankService {

	
	@Autowired
	private UserBankinfoMapper userBankinfoMapper ;
	@Autowired
	private IUserinfoService userinfoService ;
	/**
	 * 通过用户第获取绑定的银行卡信息
	 */
	public UserBankinfo getBankinfoByUser(Long id) {
		return this.userBankinfoMapper.selectByUser(id);
	}
	
	/**
	 * 用户绑定银行卡流程
	 */
	public void bindBank(UserBankinfo userBankinfo) {
		//得到当前用户判断是否已经绑定银行卡
		Userinfo current = this.userinfoService.get(UserContext.getCurrent().getId());
		if ( !current.getIsBindBank() && current.getIsRealAuth()) { //表示用户没有绑定银行卡但是已经实名认证
			UserBankinfo ub =  new UserBankinfo();
			ub.setAccountName(current.getRealName()); // 绑定银行的名称是 实名认证的名称
			ub.setAccountNumber(userBankinfo.getAccountNumber());
			ub.setBankName(userBankinfo.getBankName());
			ub.setForkName(userBankinfo.getForkName());
			ub.setLogininfo(UserContext.getCurrent());
			this.userBankinfoMapper.insert(ub);
			//修改用户绑定银行卡的状态码 
			current.addState(BitStatesUtils.OP_HAS_BIND_BANK);
			this.userinfoService.update(current);
		}
	}

	
	
	
}
