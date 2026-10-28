package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 其他支付方式。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 other。</p>
 */
@Getter
@Setter
public class Other {

    /**
     * 支付类型。
     * <p>类型：string；约束：M（必填）；基数：1；示例：[A-Z]&#123;2&#125;。</p>
     */
    private String paymentTypeCode;
}
