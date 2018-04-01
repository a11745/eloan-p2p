package com.xmg.p2p.business.mapper;

import com.xmg.p2p.business.domain.PlatformBankinfo;

import com.xmg.p2p.business.query.PlatformBankinfoQueryObject;

import java.util.List;

public interface PlatformBankinfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PlatformBankinfo record);

    PlatformBankinfo selectByPrimaryKey(Long id);

    List<PlatformBankinfo> selectAll();

    int updateByPrimaryKey(PlatformBankinfo record);
    

	int queryForCount(PlatformBankinfoQueryObject qo);

	List<PlatformBankinfo> query(PlatformBankinfoQueryObject qo);
    
}