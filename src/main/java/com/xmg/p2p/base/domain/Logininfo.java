package com.xmg.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户登录信息
 * @author Administrator
 *
 */
@Getter
@Setter
public class Logininfo  extends BaseDomain{
	public static final int STATE_NORMAL = 0;//正常
			public static final int STATE_LOCK = 1;//锁定		

	public static final int USER_MANAGER = 0;//后台用户
	public static final int USER_CLIENT = 1;//前台用户
	
	private String username;
	private String password;
	private int state;
	
	private int userType;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	
}
