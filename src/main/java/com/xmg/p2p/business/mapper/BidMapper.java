package com.xmg.p2p.business.mapper;

import com.xmg.p2p.business.domain.Bid;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface BidMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Bid record);

    Bid selectByPrimaryKey(Long id);

    List<Bid> selectAll();

    int updateByPrimaryKey(Bid record);

    /**
     * 当某同一个借款的状态发生改变的时候,所有的投标对象的状态都需要更改
     * @param bidRequestId
     * @param bidrequestStateApprovePending1
     */
	void updateBidState(@Param("bidRequestId")Long bidRequestId, @Param("state") int state);
}