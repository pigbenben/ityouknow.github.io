package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 支票支付。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 cheque。</p>
 */
@Getter
@Setter
public class Cheque {

    /**
     * 支票支付Number。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String chequeFreeText;
}
