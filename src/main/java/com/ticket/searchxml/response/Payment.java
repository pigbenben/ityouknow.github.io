package com.ticket.searchxml.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 支付明细。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 payment。</p>
 */
@Getter
@Setter
public class Payment {

    /**
     * 支付明细项。
     * <p>类型：subpayment；约束：M（必填）；基数：1-N（List）。</p>
     */
    private List<Subpayment> subpayment;
}
