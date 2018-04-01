package com.xmg.p2p.base.domain;

import com.alibaba.druid.support.logging.Log;
import com.xmg.p2p.base.util.BitStatesUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户相关信息
 * @author Administrator
 *
 */
@Getter
@Setter
public class Userinfo extends BaseDomain {
	/**
	 * UserInfo：
		属性名称	属性类型	属性说明
		Version	Int	版本号，用作乐观锁
		bitState	Long	用户状态值
		realName	String	用户实名值（冗余数据）
		idNumber	String	用户身份证号（冗余数据）
		phoneNumber	String	用户电话
		incomeGrade	SystemDictionaryItem	收入
		Marriage	SystemDictionaryItem	婚姻情况
		kidCount	SystemDictionaryItem	子女情况
		educationBackground	SystemDictionaryItem	学历
		houseCondition	SystemDictionaryItem	住房条件
	 */
	private int version; //版本号
	private long bitState;//用户状态吗
	private String realName;
	private String idNumber;
	private String phoneNumber;
	
	private String email;
	private int score;//风控累计分数
	
	private Long realAuthId;//该用户对应的实名认证对象id
	
	
	private SystemDictionaryItem incomeGrade;//收入
	private SystemDictionaryItem marriage;//
	private SystemDictionaryItem kidCount;//
	private SystemDictionaryItem educationBackground;//
	private SystemDictionaryItem houseCondition;//
	
	public void addState(long state){
		this.setBitState(BitStatesUtils.addState(this.getBitState(),state));
	}
	
	
	// 判断是否已经绑定了手机
		public boolean getIsBindPhone() {
			return BitStatesUtils.hasState(this.bitState,
					BitStatesUtils.OP_BIND_PHONE);
		}
		
		// 判断是否已经绑定看了银行卡
		public boolean getIsBindBank() {
			return BitStatesUtils.hasState(this.bitState,
					BitStatesUtils.OP_HAS_BIND_BANK);
		}

		// 判断是否已经绑定了邮箱
		public boolean getIsBindEmail() {
			return BitStatesUtils.hasState(this.bitState,
					BitStatesUtils.OP_BIND_EMAIL);
		}

		// 添加绑定的状态码
		public void addState(Long state) {
			bitState = BitStatesUtils.addState(this.bitState, state);
		}
		// 移除状态码
		public void  removeState(Long state) {
			bitState = BitStatesUtils.removeState(this.bitState, state);
		}

		// 判断用户是否已经填写了基本资料
		public boolean getIsBasicInfo() {
			return BitStatesUtils.hasState(this.bitState,
					BitStatesUtils.OP_BASIC_INFO);
		}

		// 判断用户是否已经实名认证
		public boolean getIsRealAuth() {
			return BitStatesUtils.hasState(this.bitState,
					BitStatesUtils.OP_REAL_AUTH);
		}

		// 判断用户是否已经视频认证
		public boolean getIsVedioAuth() {
			return BitStatesUtils.hasState(this.bitState,
					BitStatesUtils.OP_VEDIO_AUTH);
		}
		// 判断用户是否已经有一个借款在审核流程中
		public boolean getHasBidRequestInProcess() {
			return BitStatesUtils.hasState(this.bitState,
					BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS);
		}
		// 判断用户是否已经有一个提现在审核流程中
		public boolean getHasWithdrawInProcess() {
			return BitStatesUtils.hasState(this.bitState,
					BitStatesUtils.OP_HAS_WITHDRAW_PROCESS);
		}
}
