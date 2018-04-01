package com.xmg.p2p.base.service;

import java.util.List;
import java.util.Map;

import com.xmg.p2p.base.domain.Userinfo;

/**
 * 用户相关信息服务
 * @author Administrator
 *
 */
public interface IUserinfoService {
	/**
	 * 乐观锁实现
	 * @param userinfo
	 */
	public void update(Userinfo userinfo);

	public void add(Userinfo ui);

	public Userinfo get(Long id);
	/**
	 * 绑定手机号
	 * @param phoneNumber
	 * @param verifyCode
	 */
	public void bindPhone(String phoneNumber, String verifyCode);
	/**
	 * 发送绑定邮箱邮件
	 * @param email
	 */
	public void sendVerifyEmail(String email);
	
	/**
	 * 直接得到当前的userinfo对象
	 */
	public Userinfo getCurrent();
	/**
	 * 执行邮箱绑定
	 * @param uuid
	 */
	public void bindEmail(String uuid);
	/**
	 * 更新用户基本数据
	 * @param userinfo
	 */
	public void updateBasicInfo(Userinfo userinfo);
/**
 * 用于用户的autocomplate
 * 返回值里面MAP（id,username）
 * @param keyword
 * @return
 */
	public List<Map<String, Object>> autoComplate(String keyword);
	
}
