package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 时差。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 timeDifference。</p>
 */
@Getter
@Setter
public class TimeDifference {

    /**
     * 航空公司。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String airline;

    /**
     * 天数。
     * <p>类型：int；约束：O（可选）；基数：0-1；示例：/d。</p>
     */
    private Integer days;

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

    /**
     * 航班号。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String flightNo;

    /**
     * 小时。
     * <p>类型：int；约束：O（可选）；基数：0-1。</p>
     */
    private Integer hour;

    /**
     * 分钟数。
     * <p>类型：int；约束：O（可选）；基数：0-1；示例：/d。</p>
     */
    private Integer minutes;

    /**
     * 航班号后缀。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：[A-Z] 一位大写字母。</p>
     */
    private String suffix;
}
