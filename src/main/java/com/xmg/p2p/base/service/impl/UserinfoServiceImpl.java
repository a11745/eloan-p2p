package com.xmg.p2p.base.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.xmg.p2p.base.domain.MailVerify;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.mapper.MailVerifyMapper;
import com.xmg.p2p.base.mapper.UserinfoMapper;
import com.xmg.p2p.base.service.IMailService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.service.IVerifyCodeService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.base.util.BitStatesUtils;
import com.xmg.p2p.base.util.DateUtil;
import com.xmg.p2p.base.util.UserContext;

@Service
public class UserinfoServiceImpl implements IUserinfoService {
	
	@Autowired
	private UserinfoMapper userinfoMapper;
	
	@Autowired
	private IVerifyCodeService verifyCodeService;
	
	@Autowired
	private MailVerifyMapper mailVerifyMapper;
	
	@Value("${mail.applicationUrl}")
	private String hostUrl;
	
	@Autowired
	private IMailService mailService;
	
	@Override
	public void update(Userinfo userinfo) {
			int ret = userinfoMapper.updateByPrimaryKey(userinfo);
			if (ret == 0) {
				throw new RuntimeException("乐观锁失败，Userinfo:"+userinfo.getId());
			}
	}

	@Override
	public void add(Userinfo ui) {
		userinfoMapper.insert(ui);
		
	}

	@Override
	public Userinfo get(Long id) {
		
		return userinfoMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public void bindPhone(String phoneNumber, String verifyCode) {
		//如果用户没有绑定验证码
		Userinfo current = get(UserContext.getCurrent().getId());
		if (!current.getIsBindPhone()) {
			//验证验证码合法
			boolean ret = this.verifyCodeService.verify(phoneNumber,verifyCode);
			if (ret) {
				//如果合法 给用户绑定手机
				//current.setBitState(BitStatesUtils.addState(current.getBitState(), BitStatesUtils.OP_BIND_PHONE));
				current.addState(BitStatesUtils.OP_BIND_PHONE);
				current.setPhoneNumber(phoneNumber);
				this.update(current);
			}else {
				//否则抛出异常
				throw new RuntimeException("绑定机失败！");
				
			}
			
			
		}
		
		
	}

	@Override
	public void sendVerifyEmail(String email) {
		//当前用户没有绑定邮箱
		Userinfo current = this.getCurrent();
		if (!current.getIsBindEmail()) {
			
			String uuid = UUID.randomUUID().toString();
			//构造一封要发送的邮件
			StringBuilder content = new StringBuilder(100)
					.append("点击<a href='")
					.append(this.hostUrl)
					.append("bindEmail.do?key=")
					.append(uuid)
					.append("'/>这里</a>完成邮箱绑定，有效期为")
					.append(BidConst.EMAIL_VALID_DAY)
					.append("天");
			try {
				System.out.println("发送邮件："+email+"邮件内容："+content);
				mailService.sendMail(email, "你在XMGp2p上的验证邮箱邮件", content.toString());
				//构造一个MailVerify对象
				MailVerify mv = new MailVerify();
				mv.setEmail(email);
				mv.setSendDate(new Date());
				mv.setUserinfoId(current.getId());
				mv.setUuid(uuid);
				mailVerifyMapper.insert(mv);
				
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("验证邮件发送失败");
			}
			
		
			//执行邮件发送
		}

		
	}

	@Override
	public Userinfo getCurrent() {
		
		return this.get(UserContext.getCurrent().getId());
	}
	
	@Override
	public void bindEmail(String uuid) {
		//通过uuid得到mailVerify对象
		MailVerify mv = mailVerifyMapper.selectByUUID(uuid);
		if (mv!=null) {
			//判断用户邮箱没有绑定
			Userinfo current = get(mv.getUserinfoId());
			if (!current.getIsBindEmail()) {
				//判断有效期
				
				if (mv!=null && DateUtil.secondsBetween(mv.getSendDate(), new Date()) <= BidConst.EMAIL_VALID_DAY*24*60*60) {
					//修改用户状态码，给用户设置邮箱
					current.addState(BitStatesUtils.OP_BIND_EMAIL);
					current.setEmail(mv.getEmail());
					this.update(current);
					return;
				}
			}
		}
		throw new RuntimeException("绑定邮箱失败");
	}

	@Override
	public void updateBasicInfo(Userinfo userinfo) {
		Userinfo old = getCurrent();
		//拷贝这次要修改的内容
		old.setEducationBackground(userinfo.getEducationBackground());
		old.setHouseCondition(userinfo.getHouseCondition());
		old.setIncomeGrade(userinfo.getIncomeGrade());
		old.setKidCount(userinfo.getKidCount());
		old.setMarriage(userinfo.getMarriage());
		//判断用户状态码
		if (!old.getIsBasicInfo()) {
			old.addState(BitStatesUtils.OP_BASIC_INFO);
		}
		this.update(old);
	}

	@Override
	public List<Map<String, Object>> autoComplate(String keyword) {
		
		return this.userinfoMapper.autoComplate(keyword);
	}

}