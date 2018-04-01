package com.xmg.p2p.base.mapper;

import com.xmg.p2p.base.domain.VedioAuth;
import com.xmg.p2p.base.query.VedioAuthQueryObject;

import java.util.List;

public interface VedioAuthMapper {
    int deleteByPrimaryKey(Long id);

    int insert(VedioAuth record);

    VedioAuth selectByPrimaryKey(Long id);

    List<VedioAuth> selectAll();

    int updateByPrimaryKey(VedioAuth record);
    
    /**
     * 查询相关
     */
    int queryForCount(VedioAuthQueryObject qo);
    List<VedioAuth> query(VedioAuthQueryObject qo);
}