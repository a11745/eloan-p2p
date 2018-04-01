package com.xmg.p2p.base.service;
/**
 * 专门用来发送邮件
 * @author Administrator
 *
 */
public interface IMailService {
	/**
	 * 发送邮件
	 * @param target  目标邮件地址
	 * @param title
	 * @param content
	 */
	public void sendMail(String target,String title,String content);
}
