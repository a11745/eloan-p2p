package com.xmg.p2p.base.mapper;

import com.xmg.p2p.base.domain.RealAuth;
import com.xmg.p2p.base.query.RealAuthQueryObject;

import java.util.List;

public interface RealAuthMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RealAuth record);

    RealAuth selectByPrimaryKey(Long id);

    List<RealAuth> selectAll();

    int updateByPrimaryKey(RealAuth record);
    /* int insert(RealAuth record);

    RealAuth selectByPrimaryKey(Long id);

    int updateByPrimaryKey(RealAuth record);
    */
    //分页查询相关的
    int queryForCount (RealAuthQueryObject qo );
    
    List<RealAuth> query(RealAuthQueryObject qo ) ;
  
}