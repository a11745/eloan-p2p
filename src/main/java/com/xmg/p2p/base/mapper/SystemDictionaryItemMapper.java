package com.xmg.p2p.base.mapper;

import com.xmg.p2p.base.domain.SystemDictionaryItem;
import com.xmg.p2p.base.query.SystemDictionaryQuearyObject;

import java.util.List;

public interface SystemDictionaryItemMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SystemDictionaryItem record);

    SystemDictionaryItem selectByPrimaryKey(Long id);

    List<SystemDictionaryItem> selectAll();

    int updateByPrimaryKey(SystemDictionaryItem record);
    /**
     * 分页相关的查询
     * 
     */
    int queryForCount(SystemDictionaryQuearyObject qo);
    List<SystemDictionaryItem> query(SystemDictionaryQuearyObject qo);
/**
 * 
 * @param sn
 * @return
 */
	List<SystemDictionaryItem> listByParentSn(String sn);
}