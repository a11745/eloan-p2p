package com.xmg.p2p.base.service;

import com.xmg.p2p.base.domain.Account;

/**
 * 账户相关服务
 * @author Administrator
 *
 */
public interface IAccountService {
	
	public void update(Account account);

	public void add(Account account);

	public Account get(Long id);
	/*
	 * 得到当前用户对应的账户信息
	 */
	public Account getCurrent();
}
