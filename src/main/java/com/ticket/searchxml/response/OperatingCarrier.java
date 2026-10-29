package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 承运方航班数据。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 operatingCarrier。</p>
 */
@Getter
@Setter
public class OperatingCarrier {

    /**
     * 航班号。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String flightNo;

    /**
     * 航空公司。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String ocAirline;

    /**
     * 航班号后缀。
     * <p>类型：string；约束：M（必填）；基数：1；示例：[A-Z] 一位大写字母。</p>
     */
    private String ocFlightSuffix;

    /**
     * 承运方的RBD。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String ocRbd;
}
