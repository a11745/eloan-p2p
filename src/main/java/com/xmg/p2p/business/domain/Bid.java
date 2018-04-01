package com.xmg.p2p.business.domain;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import com.xmg.p2p.base.domain.BaseDomain;
import com.xmg.p2p.base.domain.Logininfo;

/**
 * 投标相关
 *
 */
@Setter @Getter
public class Bid extends BaseDomain{
/**
 * 投标对象：
		属性名称	属性类型	属性说明
		actualRate	BigDecimal	实际年利率(应该是等同于标的的利率)
		availableAmount	BigDecimal	投标有效金额(就是投标金额)
		bidRequestId	Long	来自于哪个借款标
		bidRequestTitle	String	标的名称（冗余数据）
		bidUser	Logininfo	投标人
		bidTime	Date	投标时间
		bidRequestState	int	标的状态，应该和标的状态同步
 */
	private BigDecimal actualRate;// 年化利率(等同于bidrequest上的currentRate)
	private BigDecimal availableAmount;// 这次投标金额
	private Long bidRequestId;// 关联借款
	private String bidRequestTitle;// 冗余数据,等同于借款标题
	private Logininfo bidUser;// 投标人
	private Date bidTime;// 投标时间
	private int bidRequestState;//  标的状态
	
	
}
