package com.xmg.p2p.business.domain;

import static com.xmg.p2p.base.util.BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1;
import static com.xmg.p2p.base.util.BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2;
import static com.xmg.p2p.base.util.BidConst.BIDREQUEST_STATE_BIDDING;
import static com.xmg.p2p.base.util.BidConst.BIDREQUEST_STATE_BIDDING_OVERDUE;
import static com.xmg.p2p.base.util.BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK;
import static com.xmg.p2p.base.util.BidConst.BIDREQUEST_STATE_PAYING_BACK;
import static com.xmg.p2p.base.util.BidConst.BIDREQUEST_STATE_PAY_BACK_OVERDUE;
import static com.xmg.p2p.base.util.BidConst.BIDREQUEST_STATE_PUBLISH_PENDING;
import static com.xmg.p2p.base.util.BidConst.BIDREQUEST_STATE_PUBLISH_REFUSE;
import static com.xmg.p2p.base.util.BidConst.BIDREQUEST_STATE_REJECTED;
import static com.xmg.p2p.base.util.BidConst.BIDREQUEST_STATE_UNDO;
import static com.xmg.p2p.base.util.BidConst.ZERO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.xmg.p2p.base.domain.BaseDomain;
import com.xmg.p2p.base.domain.Logininfo;
import com.xmg.p2p.base.util.BidConst;

import lombok.Getter;
import lombok.Setter;
/**
 * 借款对象
 * @author Administrator
 *
 */
@Getter
@Setter
public class BidRequest extends BaseDomain {
	/**
	 * 借款对象模型;
		属性名称	属性类型	属性说明
		version	int	对象版本信息（用作乐观锁）
		returnType	int	还款方式
		bidRequestType	int	借款类型，在我们项目中，就是信用借款
		bidRequestState	int	标的的状态，因为标的在一个时间只可能处于一种状态，所以使用一个int来表示即可
		bidRequestAmount	BigDecimal	这个借款的金额
		currentRate	BigDecimal	这个借款的利率
		minBidAmount	BigDecimal	这个借款允许的最小的投标金额，默认是50；
		monthes2Return	int	借款期限，就是这个借款的还款时间，单位是月，一般可供1~18选择
		bidCount	int	这个借款现在已经有多少次投标（这个数据是用于标的监控看的）
		totalRewardAmount		总回报金额（在我们的项目中，总回报金额就是这个标总利息的金额，其实在实际的项目中，标的可能还有额外的投标奖金）；
		currentSum	BigDecimal	当前已经投了多少钱了；
		Title	String	借款标题
		description	String	借款描述
		note	String	风控评审意见，这个意见是需要在标的里面展示给用户看的，这个意见也是发标前审核的审核意见；
		disableDate	Date	招标到期时间，这个是根据前台选择的招标时间和发标前审核的时间来决定的，超过这个时间，如果标的还没有投满，就需要进行流标操作；
		disableDays	int	招标天数，这个就是前台填写的招标天数；
		createUser	Logininfo	借款人
		Bids	List<Bid>	这个标的的投标记录；
		applyTime	Date	申请时间
		publishTime	Date	发布时间，这个时间在我们项目中就是发标前审核的时间，在实际项目中，可能有些标是可以设定一个时间，定时发标的；
	 */
	private int version;// 版本号
	private int returnType;// 还款类型(等额本息)
	private int bidRequestType;// 借款类型(信用标)
	private int bidRequestState;// 借款状态
	private BigDecimal bidRequestAmount;// 借款总金额
	private BigDecimal currentRate;// 年化利率
	private BigDecimal minBidAmount;// 最小借款金额
	private int monthes2Return;// 还款月数
	private int bidCount ;// 已投标次数(冗余)
	private BigDecimal totalRewardAmount;// 总回报金额(总利息)
	private BigDecimal currentSum  = BidConst.ZERO;// 当前已投标总金额
	private String title;// 借款标题
	private String description;// 借款描述
	private String note;// 风控意见
	private Date disableDate;// 招标截止日期
	private int disableDays;// 招标天数
	private Logininfo createUser;// 借款人
	private List<Bid> bids;// 针对该借款的投标
	private Date applyTime;// 申请时间
	private Date publishTime;// 发标时间
	

	//获取剩余还未投满的金额 (+:add  -:subtract * :multiply  / :divide)
	public BigDecimal  getRemainAmount(){
		return this.bidRequestAmount.subtract(this.currentSum);
	}
	
	//获取到进度条
	public BigDecimal getPersent(){
		return this.currentSum.divide(this.bidRequestAmount, BidConst.DISPLAY_SCALE, RoundingMode.HALF_UP);
		
	}
	
	public String getBidRequestStateDisplay() {
		switch (this.bidRequestState) {
		case BIDREQUEST_STATE_PUBLISH_PENDING:
			return "待发布";
		case BIDREQUEST_STATE_BIDDING:
			return "招标中";
		case BIDREQUEST_STATE_UNDO:
			return "已撤销";
		case BIDREQUEST_STATE_BIDDING_OVERDUE:
			return "流标";
		case BIDREQUEST_STATE_APPROVE_PENDING_1:
			return "满标一审";
		case BIDREQUEST_STATE_APPROVE_PENDING_2:
			return "满标二审";
		case BIDREQUEST_STATE_REJECTED:
			return "满标审核被拒";
		case BIDREQUEST_STATE_PAYING_BACK:
			return "还款中";
		case BIDREQUEST_STATE_COMPLETE_PAY_BACK:
			return "完成";
		case BIDREQUEST_STATE_PAY_BACK_OVERDUE:
			return "逾期";
		case BIDREQUEST_STATE_PUBLISH_REFUSE:
			return "发标拒绝";
		default:
			return "";
		}
	}
	
	public String getReturnTypeDisplay() {
		return returnType == BidConst.RETURN_TYPE_MONTH_INTEREST ? "按月到期"
				: "等额本息";
	}

	
	public String getJsonString() {
		Map<String, Object> json = new HashMap<>();
		json.put("id", id);
		json.put("username", this.createUser.getUsername());
		json.put("title", title);
		json.put("bidRequestAmount", bidRequestAmount);
		json.put("currentRate", currentRate);
		json.put("monthes2Return", monthes2Return);
		json.put("returnType", getReturnTypeDisplay());
		json.put("totalRewardAmount", totalRewardAmount);
		return JSONObject.toJSONString(json);
	}

}
