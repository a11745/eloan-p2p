package com.xmg.p2p.base.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 登录日志
 * @author Administrator
 *
 */

@Getter
@Setter
public class Iplog extends BaseDomain {
	
	public static final int STATE_SUCCESS = 1;
	public static final int STATE_FALLED = 0;
	
	private String ip;
	private Date loginTime;
	private String userName;
	private int state;
	
	private int userType;//用户登录类型
	
	public String getStateDisplay(){
		return state == STATE_SUCCESS ? "登陆成功":"登陆失败";
	}
	
	public String getUserTypeDisplay(){
		return userType==Logininfo.USER_CLIENT?"前端用户":"后台管理员";
	}
}
