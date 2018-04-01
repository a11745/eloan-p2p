package com.xmg.p2p.base.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.p2p.base.domain.RealAuth;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.mapper.RealAuthMapper;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.query.RealAuthQueryObject;
import com.xmg.p2p.base.service.IRealAuthService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.BitStatesUtils;
import com.xmg.p2p.base.util.UserContext;

@Service
public class RealAuthServiceImpl  implements IRealAuthService {

	

	@Autowired
	private RealAuthMapper realAuthMapper ;
	@Autowired
	private IUserinfoService userinfoService ;
	
	
	
	/**
	 * 获取到当前用户的实名认证对象
	 */
	public RealAuth get(Long id) {
		return this.realAuthMapper.selectByPrimaryKey(id);
	}

	@Override
	public void apply(RealAuth realAuth) {
		//判断当前用户没有实名认证并且当前用户不处于等待审核状态
		Userinfo current = userinfoService.getCurrent();
		if (!current.getIsRealAuth() && current.getRealAuthId()==null) {
			//保存一个实名认证对象
			realAuth.setState(RealAuth.STATE_NORMAL);
			realAuth.setApplier(UserContext.getCurrent());
			realAuth.setApplyTime(new Date());
			
			realAuthMapper.insert(realAuth);
			//吧实名认证的id设置给userinfo
			current.setRealAuthId(realAuth.getId());
			userinfoService.update(current);
		}
		
		
	}

	@Override
	public PageResult query(RealAuthQueryObject qo) {
		int count = realAuthMapper.queryForCount(qo);
		if (count>0) {
			List<RealAuth> list = realAuthMapper.query(qo);
			return new PageResult(list, count, qo.getCurrentPage(), qo.getPageSize());
		}
		return PageResult.empty(qo.getPageSize());
	}

	@Override
	public void audit(Long id, String remark, int state) {
		//根据id得到实名认证对象
		RealAuth ra = get(id);
		//如果对象存在，并且对象处于待审核状态
		if (ra!=null && ra.getState() == RealAuth.STATE_NORMAL) {
			//1.设置通用属性
			ra.setAuditor(UserContext.getCurrent());
			ra.setApplyTime(new Date());
			ra.setState(state);
			//2.如果状态是审核拒绝
			Userinfo applier = userinfoService.get(ra.getApplier().getId());
			if (state == RealAuth.STATE_AUDIT) {
				//3.如果状态是审核通过
				//1.判断用户处于未审核状态
				if (!applier.getIsRealAuth()) {
					//2.添加审核的状态码，设置userinfo上面的用于数据，重新realauthid;
					applier.addState(BitStatesUtils.OP_REAL_AUTH);
					applier.setRealName(ra.getRealName());
					applier.setIdNumber(ra.getIdNumber());
					applier.setRealAuthId(ra.getId());
				}
				
			}else {
				//1.userinfo中的reaauthid设置为空
				applier.setRealAuthId(null);
			}
			this.userinfoService.update(applier);
			realAuthMapper.updateByPrimaryKey(ra);
		}
		
	}

	
	
}
