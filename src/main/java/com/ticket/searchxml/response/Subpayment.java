package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 支付明细项。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 subpayment。</p>
 */
@Getter
@Setter
public class Subpayment {

    /**
     * 支付方式。
     * <p>类型：method；约束：M（必填）；基数：1。</p>
     */
    private Method method;

    /**
     * 总票价。
     * <p>类型：string；约束：M（必填）；基数：1；示例：3位货币代码+金额数字。</p>
     */
    private String totalAmount;
}
