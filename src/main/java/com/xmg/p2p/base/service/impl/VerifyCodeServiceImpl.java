package com.xmg.p2p.base.service.impl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.xmg.p2p.base.service.IPhoneService;
import com.xmg.p2p.base.service.IVerifyCodeService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.base.util.DateUtil;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.base.vo.VerifyCodeVO;

@Service
public class VerifyCodeServiceImpl implements IVerifyCodeService {
	
/*	@Value("${sms.username}")
	private String username;
	@Value("${sms.password}")
	private String password;
	@Value("${sms.apikey}")
	private String apiKey;
	@Value("${sms.url}")
	private String url;*/
	
	
	@Autowired
	private IPhoneService phoneService;
	
	@Override
	public void sendVerifyCode(String phoneNumber) {
		//判断当前是否能够发送短信
		//从Session中获取最后一次发送短信的事件
		VerifyCodeVO vc = UserContext.getCurrentVerifyCode();
		if (vc==null || DateUtil.secondsBetween(new Date(), vc.getLastSendTime()) > 90) {
			//正常发送验证码短信
			//生成一个验证码
			String verifyCode = UUID.randomUUID().toString().substring(0,4);
			//发送短信
			//System.out.println("给手机 "+phoneNumber+"发送验证码："+verifyCode);
			//通过URL 得到一个HTTPURLConnetion连接对象
			/*try {
				//创建一个URL对象
				URL url = new URL(this.url);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				//拼接POST请求的内容
				StringBuilder content = new StringBuilder(100)
						.append("username=").append(username)
						.append("&password=").append(password)
						.append("&apikey=").append(apiKey)
						.append("&mobile=").append(phoneNumber)
						.append("&content=")
						.append("验证码是：").append(verifyCode).append(",请在5分钟内使用");
				//发送post请求,POST或者GET一定要大写
				conn.setRequestMethod("POST");
				//设置POST请求是有请求体的
				conn.setDoOutput(true);
				//写入post请求体
				conn.getOutputStream().write(content.toString().getBytes());
				//得到响应流（其实就已经发送了）
				String response = StreamUtils.copyToString(conn.getInputStream(), Charset.forName("UTF-8"));
				if (response.startsWith("success:")) {
					//发送成功
					//把手机号码 验证码 发送时间  装配到Vo中 并保存到session
					vc = new VerifyCodeVO();
					vc.setLastSendTime(new Date());
					vc.setPhoneNumber(phoneNumber);
					vc.setVerifyCode(verifyCode);
					UserContext.putVerifyCode(vc);
				}else {
					//发送失败
					throw new RuntimeException();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("短信发送失败");
			}*/
			
			try {
	
				SendSmsResponse sendSmsResponse = phoneService.sendPhone("zkq",phoneNumber,verifyCode);
				System.out.println("Code:"+sendSmsResponse.getCode());
				if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
					//请求成功
					//把手机号码 验证码 发送时间  装配到Vo中 并保存到session
					vc = new VerifyCodeVO();
					vc.setLastSendTime(new Date());
					vc.setPhoneNumber(phoneNumber);
					vc.setVerifyCode(verifyCode);
					UserContext.putVerifyCode(vc);
				}else {
					//发送失败
					throw new RuntimeException("短信发送失败");
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("短信发送失败");
			}
			
		}else {
			throw new RuntimeException("发送过于频繁");
		}
		
	}

	@Override
	public boolean verify(String phoneNumber, String verifyCode) {
		VerifyCodeVO vc = UserContext.getCurrentVerifyCode();
		if (vc!=null //发送了验证码
				&& vc.getPhoneNumber().equals(phoneNumber)//手机号
				&& vc.getVerifyCode().equalsIgnoreCase(verifyCode)//验证码
				&& DateUtil.secondsBetween(new Date(), vc.getLastSendTime()) <= BidConst.VERIFYCODE_VAILDATE_SECOND //验证码时间小于5分钟
				) {
			return true;
		}
		return false;
	}

}
