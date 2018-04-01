package com.xmg.p2p.business.service;

import java.math.BigDecimal;
import java.util.List;

import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.business.domain.BidRequest;
import com.xmg.p2p.business.domain.BidRequestAuditHistory;
import com.xmg.p2p.business.query.BidRequestQueryObject;
import com.xmg.p2p.business.query.PaymentScheduleQueryObject;
/***
 * 借款相关
 * @author Administrator
 *
 */
public interface IBidRequestService {
	void update(BidRequest bidRequest);
	/**
	 * 判断用户是否具有申请借款的权利
	 * @return
	 */
	boolean canApplyBidRequest(Long logininfoId);
	/**
	 * 申请借款
	 * @param bidRequest
	 */
	void apply(BidRequest bidRequest);
	/**
	 * 查询
	 */
	PageResult query(BidRequestQueryObject qo);
	/**
	 * 发标前审核
	 * @param id
	 * @param remark
	 * @param state
	 */
	void publishAudit(Long id, String remark, Integer state);
	
	public BidRequest get(Long id);
	/**
	 * 根据一个标查询该标的审核历史
	 * @param id
	 * @return
	 */
	List<BidRequestAuditHistory> listAuditHistorayByBidRequest(Long id);
	
	/**
	 * 查询首页借款列表
	 * @return
	 */
	List<BidRequest> listIndex(int size);
	void bid(Long bidRequestId, BigDecimal amount);
	
	/*
	 * 满标一审流程
	 */
	void fullAudit1(Long id, String remark, int state);

	/**
	 * 满标二审流程
	 * @param id
	 * @param remark
	 * @param state
	 */
	void fullAudit2(Long id, String remark, int state);

	/**
	 * 还款列表
	 * @param qo
	 * @return
	 */
	PageResult queryForPaymentSchedule(PaymentScheduleQueryObject qo);

	/**
	 * 还款
	 * @param id
	 */
	void returnMoney(Long id);
}
