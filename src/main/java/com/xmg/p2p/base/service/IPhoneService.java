package com.xmg.p2p.base.service;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;

/**
 * 专门用来发送手机短信的
 * @author Administrator
 *
 */
public interface IPhoneService {
	/**
	 * 专门用来发送手机短信的
	 * @param PhoneNumbers //接受手机号
	 * @param SignName	//短信签名
	 * @param TemplateCode //短信模板ID（短信内容）
	 */
	//public void sendPhone(String username,String PhoneNumbers,String code);
	public SendSmsResponse sendPhone(String username,String PhoneNumbers,String code);
}
