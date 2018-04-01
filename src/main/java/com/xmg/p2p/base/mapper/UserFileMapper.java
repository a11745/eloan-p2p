package com.xmg.p2p.base.mapper;

import com.xmg.p2p.base.domain.UserFile;
import com.xmg.p2p.base.query.UserFileQueryObject;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface UserFileMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserFile record);

    UserFile selectByPrimaryKey(Long id);

    List<UserFile> selectAll();

    int updateByPrimaryKey(UserFile record);
    /**
     * 列出摸一个用户没有选择风控材料类型的风控资料对象
     * @param logininfoId
     * @param hashType 
     * @return
     */
	List<UserFile> listUnTypeFiles(@Param("logininfoId")Long logininfoId, @Param("hasType")boolean hasType);
	
	int queryForCount(UserFileQueryObject qo);
	List<UserFile> query(UserFileQueryObject qo);
}