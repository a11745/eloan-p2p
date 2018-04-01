package com.xmg.p2p.business.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;import com.oracle.jrockit.jfr.UseConstantPool;
import com.sun.glass.ui.Size;
import com.sun.javafx.css.CalculatedValue;
import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.domain.VedioAuth;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.base.util.BitStatesUtils;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.business.domain.Bid;
import com.xmg.p2p.business.domain.BidRequest;
import com.xmg.p2p.business.domain.BidRequestAuditHistory;
import com.xmg.p2p.business.domain.PaymentSchedule;
import com.xmg.p2p.business.domain.PaymentScheduleDetail;
import com.xmg.p2p.business.mapper.BidMapper;
import com.xmg.p2p.business.mapper.BidRequestAuditHistoryMapper;
import com.xmg.p2p.business.mapper.BidRequestMapper;
import com.xmg.p2p.business.mapper.PaymentScheduleDetailMapper;
import com.xmg.p2p.business.mapper.PaymentScheduleMapper;
import com.xmg.p2p.business.query.BidRequestQueryObject;
import com.xmg.p2p.business.query.PaymentScheduleQueryObject;
import com.xmg.p2p.business.service.IAccountFlowService;
import com.xmg.p2p.business.service.IBidRequestService;
import com.xmg.p2p.business.service.ISystemAccountService;
import com.xmg.p2p.business.util.CalculatetUtil;

@Service
public class BidRequestServiceImpl implements IBidRequestService {

	@Autowired
	private BidRequestMapper bidRequestMapper;
	@Autowired
	private IUserinfoService userinfoService;
	@Autowired
	private IAccountService accountService;
	@Autowired
	private BidRequestAuditHistoryMapper bidRequestAuditHistoryMapper;
	@Autowired
	private BidMapper bidMapper;
	@Autowired
	private IAccountFlowService accountFlowService;
	@Autowired
	private ISystemAccountService systemAccountService;
	@Autowired
	private PaymentScheduleMapper paymentScheduleMapper;
	@Autowired
	private PaymentScheduleDetailMapper paymentScheduleDetailMapper;
	
	@Override
	public void update(BidRequest bidRequest) {
		int ret = bidRequestMapper.updateByPrimaryKey(bidRequest);
		if (ret == 0) {
			throw new RuntimeException("乐观锁失败 bidRequest:"+bidRequest.getId());
		}

	}

	@Override
	public boolean canApplyBidRequest(Long logininfoId) {
		//得到指定用户
		//判断1.基本资料 2.实名认证 3.视频认证 4.风控资料分数 5.没有借款在流程中
		Userinfo userinfo = userinfoService.get(logininfoId);
		return 	userinfo != null 
			&& userinfo.getIsBasicInfo() 
			&& userinfo.getIsRealAuth()
			&& userinfo.getIsVedioAuth()
			&& userinfo.getScore() >= BidConst.CREDIT_BORROW_SCORE 
			&& !userinfo.getHasBidRequestInProcess(); 
	}

	@Override
	public void apply(BidRequest br) {
		Account account = accountService.getCurrent();
		//首先要满足最基本的申请条件
		//额外的判断
				//1.系统最小额度《=借款金额《=剩余信用额度
				//2.5《=利息《=50
				//3.  最小投标金额>=系统最小投标金额
		if (canApplyBidRequest(UserContext.getCurrent().getId())
				&& br.getBidRequestAmount().compareTo(BidConst.SMALLEST_BIDREQUEST_AMOUNT)>=0 //借款金额>系统最小借款金额
				&& br.getBidRequestAmount().compareTo(account.getRemainBorrowLimit())<=0 //借款金额>=信用额度
				&& br.getCurrentRate().compareTo(BidConst.SMALLEST_CURRENT_RATE) >= 0
				&& br.getCurrentRate().compareTo(BidConst.MAX_CURRENT_RATE) <=0
				&& br.getMinBidAmount().compareTo(BidConst.SMALLEST_BID_AMOUNT)>=0
				){
			//===========进入借款申请
			//1.创建一个新的BidRequest，设置相关参数
			BidRequest bidRequest = new BidRequest();

			bidRequest.setBidRequestAmount(br.getBidRequestAmount());
			bidRequest.setCurrentRate(br.getCurrentRate());
			bidRequest.setDescription(br.getDescription());
			bidRequest.setDisableDays(br.getDisableDays());
			bidRequest.setMinBidAmount(br.getMinBidAmount());
			bidRequest.setReturnType(br.getReturnType());
			bidRequest.setMonthes2Return(br.getMonthes2Return());
			bidRequest.setTitle(br.getTitle());
			
			//2.设置相关值
			bidRequest.setApplyTime(new Date());
			bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_PENDING);
			bidRequest.setCreateUser(UserContext.getCurrent());
			bidRequest.setTotalRewardAmount(CalculatetUtil.calTotalInterest(bidRequest.getReturnType(), 
					bidRequest.getBidRequestAmount(), 
					bidRequest.getCurrentRate(),
					bidRequest.getMonthes2Return()));
			//3.保存
			bidRequestMapper.insert(bidRequest);
			//给借款人添加一个状态吗
			Userinfo userinfo = userinfoService.getCurrent();
			userinfo.addState(BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS);
			userinfoService.update(userinfo);
		}
		
		
		
	}

	@Override
	public PageResult query(BidRequestQueryObject qo) {
		int count = bidRequestMapper.queryForCount(qo);
		if (count>0) {
			List<BidRequest> list = bidRequestMapper.query(qo);
			return new PageResult(list, count, qo.getCurrentPage(), qo.getPageSize());
		}
		return PageResult.empty(qo.getPageSize());
	
	}

	@Override
	public void publishAudit(Long id, String remark, Integer state) {
		//查出bidrequest
		BidRequest br = bidRequestMapper.selectByPrimaryKey(id);
		//判断状态
		if (br!=null && br.getBidRequestState() == BidConst.BIDREQUEST_STATE_PUBLISH_PENDING) {
			//创建一个审核历史对象
			BidRequestAuditHistory history = new BidRequestAuditHistory();
			history.setApplier(br.getCreateUser());
			history.setApplyTime(br.getApplyTime());
			history.setAuditType(BidRequestAuditHistory.PUBLISH_AUDIT);
			history.setAuditor(UserContext.getCurrent());
			history.setApplyTime(new Date());
			history.setRemark(remark);
			history.setState(state);
			history.setBidRequestId(br.getId());
			bidRequestAuditHistoryMapper.insert(history);
			
			if (state==BidRequestAuditHistory.STATE_AUDIT) {
				//如果审核通过，修改标的状态 ，设置风控意见
				br.setBidRequestState(BidConst.BIDREQUEST_STATE_BIDDING);
				br.setDisableDate(DateUtils.addDays(new Date(), br.getDisableDays()));
				br.setPublishTime(new Date());
				br.setNote(remark);
			}else {
				//如果审核失败 修改标的状态，用户要去掉状态码
				br.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_REFUSE);
				Userinfo applier = userinfoService.get(br.getCreateUser().getId());
				applier.removeState(BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS);
				userinfoService.update(applier);
			}
			this.update(br);
			
		}
		
		
		
		
		
	}

	@Override
	public BidRequest get(Long id) {
		
		return bidRequestMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<BidRequestAuditHistory> listAuditHistorayByBidRequest(Long id) {
		
		return bidRequestAuditHistoryMapper.listAuditHistorayByBidRequest(id);
	}

	@Override
	public List<BidRequest> listIndex(int size) {
		BidRequestQueryObject qo = new BidRequestQueryObject();
		qo.setStates(new int[]{BidConst.BIDREQUEST_STATE_BIDDING,BidConst.BIDREQUEST_STATE_PAYING_BACK,BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK});
		qo.setPageSize(size);
		qo.setCurrentPage(1);
		qo.setOrderBy("bidRequestState");
		qo.setOrderType("ASC");
		return 	bidRequestMapper.query(qo);
		
	}


	/**
	 * 前台用户投标流程
	 */
	@Override
	public void bid(Long bidRequestId, BigDecimal amount) {
		//得到当前的借款对象
		BidRequest bidRequest = this.get(bidRequestId);
		//得到当前账户
		Account account = this.accountService.get(UserContext.getCurrent().getId());
		//判断本次投标是否有效
		if (bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_BIDDING    //	1借款处于招标状态
				&& !account.getId().equals(bidRequest.getCreateUser().getId())   //	2当前用户不是投标本人
				&& amount.compareTo(bidRequest.getMinBidAmount()) >= 0  //	3投标金额  >= 最小人投标金额
				&& amount.compareTo(bidRequest.getRemainAmount()) <= 0   //	4投标金额  <= 当前标的剩余金额
				&& amount.compareTo(account.getUsableAmount())  <= 0  //	5投标金额  <= 账户可用余额
				) {
			//执行投标操作
			//	1创建一个投标对象  设置对象的相关属性
			Bid bid = new Bid();
			bid.setActualRate(bidRequest.getCurrentRate()); // 年化利率
			bid.setAvailableAmount(amount) ;  //投标金额
			bid.setBidRequestId(bidRequestId) ;  //对应的标的对象
			bid.setBidRequestState(bidRequest.getBidRequestState()); //  标的状态
			bid.setBidRequestTitle(bidRequest.getTitle()) ;
			bid.setBidTime(new Date()) ;
			bid.setBidUser(UserContext.getCurrent()) ; //投标人 
			this.bidMapper.insert(bid);
 			//	2修改借款中的一些属性
			bidRequest.setBidCount(bidRequest.getBidCount()+1) ; // 已经投标的次数
			bidRequest.setCurrentSum(bidRequest.getCurrentSum().add(amount)) ; // 当前已投标总金额
			//  3减少账户的可用余额 ,增加冻结金额
			account.setUsableAmount(account.getUsableAmount().subtract(amount)) ; 
			account.setFreezedAmount(account.getFreezedAmount().add(amount));
			this.accountService.update(account);
			//	4生成一条 投标流水
			this.accountFlowService.bidFlow(account,bid);  
			//	5判断标是否投满  如果投满了 进入满标一审状态
			if (bidRequest.getBidRequestAmount().equals(bidRequest.getCurrentSum())) {
				//设置为满标一审状态
				bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1); 
				//当借款的状态发生改变的时候,需要修改所有投标的状态
				this.bidMapper.updateBidState(bidRequestId,BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1) ;
			}
			this.update(bidRequest);
		}
	}
	/**
	 * 满标一审流程
	 */
	public void fullAudit1(Long id, String remark, int state) {
		//得到当前借款对象 ,判断借款状态
		BidRequest bidRequest = this.get(id);
		//创建一个审核历史对象 ,设置相关属性
		BidRequestAuditHistory hs = new BidRequestAuditHistory();
		hs.setApplier(bidRequest.getCreateUser());
		hs.setApplyTime(new Date());
		hs.setAuditor(UserContext.getCurrent()) ;
		hs.setAuditTime(new Date());
		hs.setAuditType(BidRequestAuditHistory.FULL_AUDIT1); // 设置为 满标一审
		hs.setBidRequestId(id);
		hs.setRemark(remark);
		hs.setState(state);
		this.bidRequestAuditHistoryMapper.insert(hs);
		//判断审核状态
		if (state == BidRequestAuditHistory.STATE_AUDIT ) {
			//审核通过 	,修改表的状态   修改投标状态
			bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
			this.bidMapper.updateBidState(id, BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
		}else{
			//审核失败,修改标的状态和投标状态
			bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_REJECTED);
			this.bidMapper.updateBidState(id,BidConst.BIDREQUEST_STATE_REJECTED);
			//遍历投标,针对每个投标.增加对应账户的可用金额  减少冻结金额  
			//方式1  性能太差 发送过多的sql
		    /*for (Bid bid : bidRequest.getBids()) {
				Account bidAccount = this.accountService.get(bid.getBidUser().getId());//得到投标对象的账户
				bidAccount.setUsableAmount(bidAccount.getUsableAmount().add(bid.getAvailableAmount()));
				bidAccount.setFreezedAmount(bidAccount.getFreezedAmount().subtract(bid.getAvailableAmount()));
				this.accountFlowService.cancelBidFlow(bidAccount,bid); //创建一条取消投标流水
				this.accountService.update(bidAccount);
			}*/
			//方式2 先查map中 如果没有再去数据库中查 然后再放到map中 减少查account的次数
			returnMoney(bidRequest);
			//对于借款人 ,移除借款状态码
			Userinfo borrowUserinfo = this.userinfoService.get(bidRequest.getCreateUser().getId());
			borrowUserinfo.removeState(BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS);
			this.userinfoService.update(borrowUserinfo);
		}
		this.update(bidRequest); //这里不能使用bidMapper 有共享锁
	}
	
	
	private void returnMoney(BidRequest bidRequest) {
		Map<Long, Account> updates = new HashMap<>();
		for (Bid bid : bidRequest.getBids()) {
			Long bidAccountId = bid.getBidUser().getId();
			Account bidAccount = updates.get(bidAccountId); // 现在map中查找
			if (bidAccount == null ) {       //map中没有查到
				bidAccount = this.accountService.get(bidAccountId);  //再查数据库
				updates.put(bidAccountId, bidAccount) ; //将查到的对象放到map中
			}
			bidAccount.setUsableAmount(bidAccount.getUsableAmount().add(bid.getAvailableAmount()));
			bidAccount.setFreezedAmount(bidAccount.getFreezedAmount().subtract(bid.getAvailableAmount()));
			this.accountFlowService.cancelBidFlow(bidAccount,bid); //创建一条取消投标流水
		}
		//再统一去修改投标人对应的账户
		for (Account bidAccount : updates.values()) {
			this.accountService.update(bidAccount);
		}
	}

	/**
	 * 满标二审流程
	 */
	public void fullAudit2(Long id, String remark, int state) {
		//得到当期借款对象 判断状态 
		BidRequest bidRequest = this.get(id);
		if (bidRequest != null
				&& bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2) {
		//创建一个审核历史对象  设置值  
		BidRequestAuditHistory hs = new BidRequestAuditHistory();
		hs.setApplier(bidRequest.getCreateUser());
		hs.setApplyTime(new Date());
		hs.setAuditor(UserContext.getCurrent()) ;
		hs.setAuditTime(new Date());
		hs.setAuditType(BidRequestAuditHistory.FULL_AUDIT2); // 设置为 满标二审
		hs.setBidRequestId(id);
		hs.setRemark(remark);
		hs.setState(state);
		this.bidRequestAuditHistoryMapper.insert(hs);
		if (state == BidRequestAuditHistory.STATE_AUDIT) {//审核通过 
			//1 从借款人的角度来
			bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_PAYING_BACK) ;//*1修改借款的状态 
			this.bidMapper.updateBidState(id, BidConst.BIDREQUEST_STATE_PAYING_BACK);	//*2修改投标的状态
			//2从借款人角度
				//*1借款人账户余额增加 , 生成同一条账户流水 
			Account borrowAccount = this.accountService.get(bidRequest.getCreateUser().getId());
			borrowAccount.setUsableAmount(borrowAccount.getUsableAmount().add(bidRequest.getBidRequestAmount()));
			this.accountFlowService.borrowSuccessFlow(borrowAccount,bidRequest);
				//*2去掉借款的状态码 (两处都用到放在外面)
				//*3剩余信用额度减少
			borrowAccount.setRemainBorrowLimit(borrowAccount.getRemainBorrowLimit().subtract(bidRequest.getBidRequestAmount()));
				//*4增加unReturnAmount总额  (账户待还金额)
			borrowAccount.setUnReturnAmount(borrowAccount.getUnReturnAmount().add(bidRequest.getTotalRewardAmount())
					.add(bidRequest.getBidRequestAmount()));
				//*5支付借款手续费,生成手续费流水
			BigDecimal borrowChargeFee = CalculatetUtil.calAccountManagementCharge(bidRequest.getBidRequestAmount());
			this.accountFlowService.borrowChargeFeeFlow(borrowChargeFee,borrowAccount ,bidRequest);  // 生成的手续费流水
				//*6平台收取手续费(两个对象 --> 账户和账户流水)
			this.systemAccountService.chargeBorrowFee(borrowChargeFee , bidRequest);
//borrowAccount.setUsableAmount(borrowAccount.getUsableAmount().add(bidRequest.getBidRequestAmount()).subtract(borrowChargeFee));
			this.accountService.update(borrowAccount) ;
			//3从投资人角度
			//*1.遍历投标
			Map<Long , Account> updates = new HashMap<>();
			for (Bid bid : bidRequest.getBids()) {
				//*2减少账户的冻结金额,增加投标成功的流水
				Long bidAccountId = bid.getBidUser().getId();
				Account bidAccount = updates.get(bidAccountId);
				if (bidAccount  == null ) {
					bidAccount =this.accountService.get(bidAccountId);
					updates.put(bidAccountId, bidAccount);
				}
				bidAccount.setFreezedAmount(bidAccount.getFreezedAmount().subtract(bid.getAvailableAmount()));
				this.accountFlowService.bidSuccessFlow(bidAccount,bid);
			}
			//4满标审核对还款流程 : 生成针对这个借款的还款信息和回款信息
			List<PaymentSchedule> pss = createPaymentSchedule(bidRequest);
			
				//*3增加待收利息和待收本金 
					//遍历还款对象和回款对象
			for (PaymentSchedule ps : pss) {
				for (PaymentScheduleDetail psd : ps.getPaymentScheduleDetails()) {
					Account bidAccount = updates.get(psd.getToLogininfoId());  //得到收款人的账户
					//待收本金
					System.out.println("77777777777777");
					System.out.println("666666666"+bidAccount);
					bidAccount.setUnReceivePrincipal(bidAccount.getUnReceivePrincipal().add(psd.getPrincipal()));
					System.out.println("888888888888888888"+bidAccount);
					bidAccount.setUnReceiveInterest(bidAccount.getUnReceiveInterest().add(psd.getInterest())) ;
				}
				
			}
			for (Account account : updates.values()) {
				this.accountService.update(account);
			}
		}else{
		//审核失败  (同满标一审失败)
			//*1修改标的状态 
			bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_REJECTED);
			//*2修改投标状态
			this.bidMapper.updateBidState(id, BidConst.BIDREQUEST_STATE_REJECTED) ;
			//*3遍历投标,每个投标人的账户可用金额增加 冻结金额减少 , 创建一条取消投标流水
			this.returnMoney(bidRequest);
		}
		//*4对于借款人  移除对应的状态码(满标二审不管成功还是失败都要移除状态码)
		Userinfo borrowUserinfo = this.userinfoService.get(bidRequest.getCreateUser().getId());
		borrowUserinfo.removeState(BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS) ;
		this.userinfoService.update(borrowUserinfo) ;
		this.update(bidRequest);
	 }
	}
	/**
	 * 创建针对这个借款的还款信息和汇款信息
	 */
	
	private List<PaymentSchedule>  createPaymentSchedule(BidRequest bidRequest){
		
		List<PaymentSchedule> ret = new ArrayList<>() ;
		//用于 累加本金
		BigDecimal totalPrincipal = BidConst.ZERO ;
		//用于累加利息
		BigDecimal totalInterest = BidConst.ZERO ;
		for (int i = 0; i < bidRequest.getMonthes2Return(); i++) {
			//每期都是一个还款对象
			PaymentSchedule ps = new PaymentSchedule();
			ps.setBidRequestId(bidRequest.getId());
			ps.setBidRequestTitle(bidRequest.getTitle());
			ps.setBidRequestType(bidRequest.getBidRequestType());
			ps.setBorrowUser(bidRequest.getCreateUser());
			ps.setDeadLine(DateUtils.addMonths(bidRequest.getPublishTime(), i+1));
			ps.setMonthIndex(i+1);
			ps.setReturnType(bidRequest.getReturnType());
			ps.setState(BidConst.PAYMENT_STATE_NORMAL);
			if (i<bidRequest.getMonthes2Return() - 1) {
				//每期要还款的利息
				ps.setInterest(CalculatetUtil.calMonthlyInterest(bidRequest.getReturnType(), bidRequest.getBidRequestAmount(),
						bidRequest.getCurrentRate(), i+1, bidRequest.getMonthes2Return()));
				// 每期还款总金额，利息 +本金
				ps.setTotalAmount(CalculatetUtil.calMonthToReturnMoney(bidRequest.getReturnType(), bidRequest.getBidRequestAmount(),
						bidRequest.getCurrentRate(), i+1, bidRequest.getMonthes2Return()));
				//本期还款本金   总的还款 - 利息
				ps.setPrincipal(ps.getTotalAmount().subtract(ps.getInterest()));
				
				totalPrincipal=totalPrincipal.add(ps.getPrincipal());
				totalInterest=totalInterest.add(ps.getInterest());
			}else{
				//最后一期
				ps.setInterest(bidRequest.getTotalRewardAmount().subtract(totalInterest));
				ps.setPrincipal(bidRequest.getBidRequestAmount().subtract(totalPrincipal));
				ps.setTotalAmount(ps.getInterest().add(ps.getPrincipal())) ;
			}
			this.paymentScheduleMapper.insert(ps);
			createPaymentScheduleDetail(ps,bidRequest); //创建每期还款对象对于的汇款明细
			ret.add(ps);
		}
		return ret ;
	}

	/**
	 * 创建针对每一期还款的回款对象
	 * @param ps
	 * @param bidRequest
	 */
	
	private void createPaymentScheduleDetail(PaymentSchedule ps , BidRequest bidRequest) {
		//用于 累加 本期还款本金
		BigDecimal totalPrincipal = BidConst.ZERO ;
		//用于累加 总金额 (本金+利息)
		BigDecimal totalAmount = BidConst.ZERO ;
		for (int i = 0; i < bidRequest.getBids().size(); i++) {
			Bid bid = bidRequest.getBids().get(i);
			//针对每一个投标创建已个回款对象
			PaymentScheduleDetail psd = new PaymentScheduleDetail();
			psd.setBidAmount(bid.getAvailableAmount());
			psd.setBidRequestId(bidRequest.getId());
			psd.setBidId(bid.getId());
			psd.setDeadline(ps.getDeadLine());
			psd.setFromLogininfo(bidRequest.getCreateUser());
			psd.setMonthIndex(i+1) ;
			psd.setToLogininfoId(bid.getBidUser().getId());
			psd.setPaymentScheduleId(ps.getId());
			psd.setReturnType(bidRequest.getReturnType());
			if ( i < bidRequest.getBids().size()-1) {
				 // 回款本金 = 投标金额 / 借款金额 * 本期还款本金
				psd.setPrincipal(bid.getAvailableAmount().divide(bidRequest.getBidRequestAmount(),
						BidConst.CAL_SCALE, RoundingMode.HALF_UP).multiply(ps.getPrincipal()
								.setScale(BidConst.CAL_SCALE, RoundingMode.HALF_UP)));
				// 汇款利息 = 投标金额/ 借款金额 * 本期还款利息
				psd.setInterest(bid.getAvailableAmount().divide(bidRequest.getBidRequestAmount(),
						BidConst.CAL_SCALE, RoundingMode.HALF_UP).multiply(ps.getInterest()
								.setScale(BidConst.CAL_SCALE, RoundingMode.HALF_UP)));
				psd.setTotalAmount(psd.getInterest().add(psd.getPrincipal()));
				
				totalPrincipal=totalPrincipal.add(psd.getPrincipal());   //本金
				totalAmount=totalAmount.add(psd.getTotalAmount());		//本息
				
			}else{
				//最后一个回款明细
				psd.setPrincipal(ps.getPrincipal().subtract(totalPrincipal)); //本期的剩余本金
				psd.setTotalAmount(ps.getTotalAmount().subtract(totalAmount)); //本期的剩余本息
				psd.setInterest(psd.getTotalAmount().subtract(psd.getPrincipal()));   //剩余的利息
			}
		this.paymentScheduleDetailMapper.insert(psd);
		ps.getPaymentScheduleDetails().add(psd);
		}
	}
	/**
	 * 还款列表
	 */
	public PageResult queryForPaymentSchedule(PaymentScheduleQueryObject qo) {
		int  count = this.paymentScheduleMapper.queryForCount(qo);
		if (count > 0) {
			List<PaymentSchedule> list = this.paymentScheduleMapper.query(qo);
			return new PageResult(list, count, qo.getCurrentPage(), qo.getPageSize());
		}
		return PageResult.empty(qo.getPageSize());
	}

	/**
	 * 还款流程
	 */
	public void returnMoney(Long id) {
		//得到还款对象  判断状态
		PaymentSchedule ps = this.paymentScheduleMapper.selectByPrimaryKey(id);
		//1处于待还款  ,  并且当前是为自己的借款还款 
		if (ps.getState()==BidConst.PAYMENT_STATE_NORMAL
				&& ps.getBorrowUser().getId().equals(UserContext.getCurrent().getId())) {
			//2借款人账户余额大于还款金额 
			Account returnAccount = this.accountService.get(ps.getBorrowUser().getId());
			if (returnAccount.getUsableAmount().compareTo(ps.getTotalAmount()) >= 0) {
		//执行还款
			//1对于还款对象,修改状态
				ps.setState(BidConst.PAYMENT_STATE_DONE);
				ps.setPayDate(new Date());
				this.paymentScheduleMapper.updateByPrimaryKey(ps);
			//2对于借款人
				//**2.1可用金额件少生成还款流水
				returnAccount.setUsableAmount(returnAccount.getUsableAmount().subtract(ps.getTotalAmount()));
				this.accountFlowService.returnMoneyFlow(returnAccount,ps);
				//**2.2待还金额减少 剩余信用额度增加
				returnAccount.setUnReturnAmount(returnAccount.getUnReturnAmount().subtract(ps.getTotalAmount()));
				returnAccount.setRemainBorrowLimit(returnAccount.getRemainBorrowLimit().add(ps.getPrincipal()));
			//3对于投资人
				//**3.1遍历还款明细对象
				Map<Long, Account> updates = new HashMap<>();
				for (PaymentScheduleDetail  psd : ps.getPaymentScheduleDetails()) {
					Long bidAccountId = psd.getToLogininfoId();
					Account bidAccount = updates.get(bidAccountId);
					if (bidAccount == null ) {
						bidAccount=this.accountService.get(bidAccountId);
						 updates.put(bidAccountId, bidAccount);
					}
				//**3.2的到投资人对象,增加账户的可用余额.生成收款流水
					bidAccount.setUsableAmount(bidAccount.getUsableAmount().add(psd.getTotalAmount()));
					this.accountFlowService.receiveMoneyFlow(bidAccount,psd);
				//**3.3减少待收本金和待收利息
					bidAccount.setUnReceivePrincipal(bidAccount.getUnReceivePrincipal().subtract(psd.getPrincipal()));
					bidAccount.setUnReceiveInterest(bidAccount.getUnReceiveInterest().subtract(psd.getInterest()));
				//**3.4支付利息管理 费,生成的支付利息管理费流水
					BigDecimal interestChargeFee = CalculatetUtil.calInterestManagerCharge(psd.getInterest());
					bidAccount.setUsableAmount(bidAccount.getUsableAmount().subtract(interestChargeFee));
					this.accountFlowService.interestChargeFeeFlow(bidAccount,interestChargeFee ,psd) ;
				//**3.5系统账户收取利息管理费
					this.systemAccountService.chargeInterestFee(interestChargeFee,psd) ;
				//修改每一期回款
					psd.setPayDate(new Date());
					this.paymentScheduleDetailMapper.updateByPrimaryKey(psd) ;
				}
				for (Account  account : updates.values()) {
					this.accountService.update(account);
				}
			//4对于借款,如果当前还的是最后一期,借款状态变成已完成状态 ,修改投标信息
				PaymentScheduleQueryObject qo = new PaymentScheduleQueryObject();
				qo.setBidRequestId(ps.getBidRequestId());
				qo.setPageSize(-1) ;
				List<PaymentSchedule>  psss = this.paymentScheduleMapper.query(qo);
				System.out.println(psss);  //空
				//遍历查询是否还有没有还款的还款计划 
				boolean unReturn = false ;
				System.out.println("66666666666666");
				for (PaymentSchedule temp : psss) {
					System.out.println("7777777777");
					if (temp.getState() == BidConst.PAYMENT_STATE_NORMAL
							|| temp.getState() == BidConst.PAYMENT_STATE_OVERDUE) {
						unReturn = true ;
						break ;
					}
				}
				System.out.println(unReturn);
				if ( !unReturn) { //说明是最后一期
					BidRequest bidRequest =this.get(ps.getBidRequestId());
					bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK);
					this.update(bidRequest);
					this.bidMapper.updateBidState(bidRequest.getId(), BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK) ;
				}
				this.accountService.update(returnAccount);
			}
		}
	}

}
