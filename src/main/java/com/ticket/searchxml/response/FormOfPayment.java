package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 支付形式。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 formOfPayment。</p>
 */
@Getter
@Setter
public class FormOfPayment {

    /**
     * 文本。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String text;
}
