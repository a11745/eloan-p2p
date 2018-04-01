package com.xmg.p2p.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.base.mapper.AccountMapper;
import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.util.UserContext;

@Service
public class AccountServiceImpl implements IAccountService {
	
	@Autowired
	private AccountMapper accountMapper;
	
	@Override
	public void update(Account account) {
		int ret = accountMapper.updateByPrimaryKey(account);
		if (ret == 0) {
			//乐观锁失败
			throw new RuntimeException("乐观锁失败，Account："+account.getId());
		}
	}

	@Override
	public void add(Account account) {
		accountMapper.insert(account);
		
	}

	@Override
	public Account get(Long id) {
		
		return accountMapper.selectByPrimaryKey(id);
	}

	@Override
	public Account getCurrent() {
		
		return this.get(UserContext.getCurrent().getId());
		
	}

}
