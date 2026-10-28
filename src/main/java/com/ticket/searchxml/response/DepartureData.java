package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 离港数据。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 departureData。</p>
 */
@Getter
@Setter
public class DepartureData {

    /**
     * 离港机场。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：aaa。</p>
     */
    private String departureAirport;

    /**
     * 起飞日期。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：YYYY-MM-DD。</p>
     */
    private String departureDate;

    /**
     * 起飞时间。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：HH:MM。</p>
     */
    private String departureTime;
}
