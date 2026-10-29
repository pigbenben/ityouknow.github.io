package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 出票航空公司信息。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 issuingAirlineInfo。</p>
 */
@Getter
@Setter
public class IssuingAirlineInfo {

    /**
     * 航空公司。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String airline;

    /**
     * 航空公司名。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String airlineName;

    /**
     * 预订系统代码。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String bookingSystemCode;
}
