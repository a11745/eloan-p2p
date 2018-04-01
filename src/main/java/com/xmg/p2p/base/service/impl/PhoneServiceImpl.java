package com.xmg.p2p.base.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.sun.tools.classfile.Annotation.element_value;
import com.xmg.p2p.base.service.IPhoneService;

@Service
public class PhoneServiceImpl implements IPhoneService {
	
	@Value("${AccessKeyId}")
	private String mYAccessKeyId;
	@Value("${AccessKeySecret}")
	private String mYAccessKeySecret;
	@Value("${SignName}")
	private String SignName;
	@Value("${TemplateCode}")
	private String TemplateCode;
	
	
	/*@Override
	public void sendPhone(String username,String PhoneNumbers,String code) {
		try {
			//2: 编写样例程序
			//注：有备注无需修改的位置请勿改动。
			//设置超时时间-可自行调整
			System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
			System.setProperty("sun.net.client.defaultReadTimeout", "10000");
			//初始化ascClient需要的几个参数
			final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
			final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
			//替换成你的AK
			final String accessKeyId = mYAccessKeyId;//你的accessKeyId,参考本文档步骤2
			final String accessKeySecret = mYAccessKeySecret;//你的accessKeySecret，参考本文档步骤2
			//初始化ascClient,暂时不支持多region（请勿修改）
			IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
			accessKeySecret);
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
			IAcsClient acsClient = new DefaultAcsClient(profile);
			 //组装请求对象
			 SendSmsRequest request = new SendSmsRequest();
			 //使用post提交
			 request.setMethod(MethodType.POST);
			 request.setEncoding("UTF-8");
			 //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
			 request.setPhoneNumbers(PhoneNumbers);
			 //必填:短信签名-可在短信控制台中找到
			 request.setSignName(SignName);
			 //必填:短信模板-可在短信控制台中找到
			 request.setTemplateCode(TemplateCode);
			 
			 //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
			 //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
			 request.setTemplateParam("{\"name\":\"111\", \"code\":\""+code+"\"}");
			 //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
			 //request.setSmsUpExtendCode("90997");
			 //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
	  	//	 request.setOutId("yourOutId");
			//请求失败这里会抛ClientException异常
			System.out.println("============");
			try {
				SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
				
				System.out.println("Code:"+sendSmsResponse.getCode());
				if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
					
					//请求成功
					System.out.println("请求成功");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			

		} catch (Exception e) {
			
		}
		
	}*/

	@Override
	public SendSmsResponse sendPhone(String username,String PhoneNumbers,String code) {
		try {
			//2: 编写样例程序
			//注：有备注无需修改的位置请勿改动。
			//设置超时时间-可自行调整
			System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
			System.setProperty("sun.net.client.defaultReadTimeout", "10000");
			//初始化ascClient需要的几个参数
			final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
			final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
			//替换成你的AK
			final String accessKeyId = mYAccessKeyId;//你的accessKeyId,参考本文档步骤2
			final String accessKeySecret = mYAccessKeySecret;//你的accessKeySecret，参考本文档步骤2
			//初始化ascClient,暂时不支持多region（请勿修改）
			IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
			accessKeySecret);
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
			IAcsClient acsClient = new DefaultAcsClient(profile);
			 //组装请求对象
			 SendSmsRequest request = new SendSmsRequest();
			 //使用post提交
			 request.setMethod(MethodType.POST);
			 request.setEncoding("UTF-8");
			 //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
			 request.setPhoneNumbers(PhoneNumbers);
			 //必填:短信签名-可在短信控制台中找到
			 request.setSignName(SignName);
			 //必填:短信模板-可在短信控制台中找到
			 request.setTemplateCode(TemplateCode);
			 
			 //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
			 //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
			 request.setTemplateParam("{\"name\":\"111\", \"code\":\""+code+"\"}");
			 //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
			 //request.setSmsUpExtendCode("90997");
			 //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
	  	//	 request.setOutId("yourOutId");
			//请求失败这里会抛ClientException异常
			 SendSmsResponse sendSmsResponse = new SendSmsResponse();
			try {
				sendSmsResponse = acsClient.getAcsResponse(request);
				
				//System.out.println("Code:"+sendSmsResponse.getCode());
				return sendSmsResponse;
			} catch (Exception e) {
				e.printStackTrace();
				return sendSmsResponse;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("发送失败");
		}
		
	}

}
