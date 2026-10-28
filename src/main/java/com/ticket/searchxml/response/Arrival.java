package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 到达信息。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 arrival。</p>
 */
@Getter
@Setter
public class Arrival {

    /**
     * 到达机场。
     * <p>类型：string；约束：M（必填）；基数：1；示例：aaa。</p>
     */
    private String arrivalAirport;

    /**
     * 到达日期。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：YYYY-MM-DD。</p>
     */
    private String arrivalDate;

    /**
     * 到达航站楼信息。
     * <p>类型：arrivalTerminalInfo；约束：O（可选）；基数：0-1。</p>
     */
    private ArrivalTerminalInfo arrivalTerminalInfo;

    /**
     * 到达时间。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：HH:MM。</p>
     */
    private String arrivalTime;

    /**
     * 目的城市。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String destinationCityCode;
}
