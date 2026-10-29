package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 支付明细。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 payment。原 sub 节点已去掉，其属性上收到本类。</p>
 */
@Getter
@Setter
public class Payment {

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
