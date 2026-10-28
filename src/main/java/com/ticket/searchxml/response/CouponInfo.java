package com.ticket.searchxml.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 票联信息。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 couponInfo。</p>
 */
@Getter
@Setter
public class CouponInfo {

    /**
     * 票联明细。
     * <p>类型：subcouponInfo；约束：O（可选）；基数：0-N（List）。</p>
     */
    private List<SubcouponInfo> subcouponInfo;
}
