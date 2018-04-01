package com.xmg.p2p.base.service;

import com.xmg.p2p.base.domain.Logininfo;

/**
 * 登录服务
 * @author Administrator
 *
 */
public interface ILogininfoService {
	public void register(String username,String password);

	public boolean checkUsername(String username);

	public Logininfo login(String username, String password,String ip,int userType);
	//初始换第一个管理员
	public void initAdmin();
}
