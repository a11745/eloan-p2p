package com.xmg.p2p.base.util;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xmg.p2p.base.domain.Logininfo;
import com.xmg.p2p.base.vo.VerifyCodeVO;

/**
 * 用于存放当前用户的上下文
 * @author Administrator
 *
 */
public class UserContext {
	public static final String USER_IN_SESSION = "logininfo";
	public static final String VERIFTCODE_IN_SESSION = "verifycode_in_session";
	/**
	 * 方向获取request
	 * @return
	 */
	private static HttpSession getSesssion(){
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
	}
	
	public static void putCurrent(Logininfo current){
		getSesssion().setAttribute(USER_IN_SESSION, current);;
	}
	public static Logininfo getCurrent(){
		
		return (Logininfo) getSesssion().getAttribute(USER_IN_SESSION);
	}
	
	
	
	/**
	 * 用来得到设置自的短信验证码
	 * @return
	 */
	public static void putVerifyCode(VerifyCodeVO vc){
		getSesssion().setAttribute(VERIFTCODE_IN_SESSION, vc);
	}
	/**
	 * 用来得到当前的短信验证码
	 * @return
	 */
	public static VerifyCodeVO getCurrentVerifyCode(){
		return (VerifyCodeVO) getSesssion().getAttribute(VERIFTCODE_IN_SESSION);
	}
}
