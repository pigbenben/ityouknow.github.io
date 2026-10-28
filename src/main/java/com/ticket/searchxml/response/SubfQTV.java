package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 常旅客明细。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 subfQTV。</p>
 */
@Getter
@Setter
public class SubfQTV {

    /**
     * 常旅客账户。
     * <p>类型：account；约束：O（可选）；基数：0-1。</p>
     */
    private Account account;

    /**
     * 航空公司。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String airline;
}
