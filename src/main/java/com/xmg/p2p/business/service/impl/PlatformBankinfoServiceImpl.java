package com.xmg.p2p.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.business.domain.PlatformBankinfo;
import com.xmg.p2p.business.domain.RechargeOffline;
import com.xmg.p2p.business.mapper.PlatformBankinfoMapper;
import com.xmg.p2p.business.query.PlatformBankinfoQueryObject;
import com.xmg.p2p.business.service.IPlatformBankinfoService;

/**
 * 平台账户相关服务
 * @author Linv999
 *
 */
@Service
public class PlatformBankinfoServiceImpl implements IPlatformBankinfoService {
	
	@Autowired
	private PlatformBankinfoMapper platformBankinfoMapper ;

	public PageResult query(PlatformBankinfoQueryObject qo) {
		int  count = this.platformBankinfoMapper.queryForCount(qo);
		if (count > 0) {
			List<PlatformBankinfo> list = this.platformBankinfoMapper.query(qo);
			return new PageResult(list, count, qo.getCurrentPage(), qo.getPageSize());
		}
		return PageResult.empty(qo.getPageSize());
	}

	/**
	 * 账户的保存或者更新
	 */
	public void saveOrUpdate(PlatformBankinfo platformBankinfo) {
		if (platformBankinfo.getId() != null ) {  //更新操作
			this.platformBankinfoMapper.updateByPrimaryKey(platformBankinfo);
		}else{
			this.platformBankinfoMapper.insert(platformBankinfo);
		}
	}

	public List<PlatformBankinfo> listAll() {
		return this.platformBankinfoMapper.selectAll();
	}

	

	
	

}
