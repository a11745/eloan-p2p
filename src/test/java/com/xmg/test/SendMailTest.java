package com.xmg.test;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.xmg.p2p.base.service.IMailService;
import com.xmg.p2p.base.service.IPhoneService;

/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")*/
public class SendMailTest {
	
	@Autowired
	private IMailService mailService;
	
	@Autowired
	private IPhoneService phoneService;
	
	/*@Test
	public void testSend(){
		mailService.sendMail("651794863@qq.com", "你在XMGp2p上的验证邮箱邮件", "点击<a href='xxx.do'>这里</a>完成邮箱验证");
	}*/
	
/*	@Test
	public void testSendPhone(){
		
		//生成一个验证码
		String verifyCode = UUID.randomUUID().toString().substring(0,4);
		
		SendSmsResponse smsResponse = phoneService.sendPhone("帅哥","13039976583",  verifyCode);
		System.out.println(smsResponse.getCode());
	}*/
}
