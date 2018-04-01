package com.xmg.p2p.business.mapper;

import com.xmg.p2p.business.domain.BidRequest;
import com.xmg.p2p.business.domain.BidRequestAuditHistory;
import com.xmg.p2p.business.query.BidRequestQueryObject;

import java.util.List;

public interface BidRequestAuditHistoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BidRequestAuditHistory record);

    BidRequestAuditHistory selectByPrimaryKey(Long id);

    List<BidRequestAuditHistory> selectAll();

    int updateByPrimaryKey(BidRequestAuditHistory record);
    
    /**
     * 根据一个标查询该标的审核历史
     * @param id
     * @return
     */
    List<BidRequestAuditHistory> listAuditHistorayByBidRequest(Long id);
}