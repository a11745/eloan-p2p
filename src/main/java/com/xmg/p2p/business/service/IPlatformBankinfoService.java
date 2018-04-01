package com.xmg.p2p.business.service;

import java.util.List;

import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.business.domain.PlatformBankinfo;
import com.xmg.p2p.business.domain.RechargeOffline;
import com.xmg.p2p.business.query.PlatformBankinfoQueryObject;

/**
 * 平台相关服务
 *
 */
public interface IPlatformBankinfoService {

	
	PageResult query(PlatformBankinfoQueryObject qo) ;

	void saveOrUpdate(PlatformBankinfo platformBankinfo);

	List<PlatformBankinfo> listAll();

	
	
}
