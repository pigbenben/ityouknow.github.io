package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 现金支付。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 cash。</p>
 */
@Getter
@Setter
public class Cash {

    /**
     * 文本。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String text;
}
