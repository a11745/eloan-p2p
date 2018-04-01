package com.xmg.p2p.base.mapper;

import com.xmg.p2p.base.domain.Logininfo;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.junit.runners.Parameterized.Parameter;

public interface LogininfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Logininfo record);

    Logininfo selectByPrimaryKey(Long id);

    List<Logininfo> selectAll();

    int updateByPrimaryKey(Logininfo record);
    
    /**
     * 根据用户名查询用户数量
     * @param username
     * @return
     */
    int getCountByUsername(String username);

	Logininfo login(@Param("username")String username, @Param("password")String password,@Param("userType")int userType);
	/**
	 * 按照类型查询用户类型数量
	 * @param userManager
	 * @return
	 */
	int selectByUserType(int userManager);
}