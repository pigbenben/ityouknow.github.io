package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 操作状态。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 status。</p>
 */
@Getter
@Setter
public class Status {

    /**
     * 控制权航空公司代码。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：a2。</p>
     */
    private String airportControlAirline;

    /**
     * 航段状态。
     * <p>类型：string；约束：M（必填）；基数：1；示例：X。</p>
     */
    private String couponStatus;
}
