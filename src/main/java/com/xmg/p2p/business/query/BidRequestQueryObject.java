package com.xmg.p2p.business.query;

import lombok.Getter;
import lombok.Setter;

import com.xmg.p2p.base.query.QueryObject;

/**
 * 借款查询对象
 *
 */
@Setter @Getter 
public class BidRequestQueryObject extends QueryObject{

	/**
	 * 借款状态
	 */
	private int bidRequestState = -1  ;
	
	/**
	 * 多个借款状态
	 */
	private int[] states ;
	
	private String orderBy ;  //按照哪个列排序 
	private String orderType;  //按照什么顺序配列
	
	
}
