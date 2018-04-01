package com.xmg.p2p.base.mapper;

import com.xmg.p2p.base.domain.Userinfo;
import java.util.List;
import java.util.Map;

public interface UserinfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Userinfo record);

    Userinfo selectByPrimaryKey(Long id);

    List<Userinfo> selectAll();

    int updateByPrimaryKey(Userinfo record);

	List<Map<String, Object>> autoComplate(String keyword);
}