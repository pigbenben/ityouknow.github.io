package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 市场方承运信息。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 marketingCarrierInfo。</p>
 */
@Getter
@Setter
public class MarketingCarrierInfo {

    /**
     * 航空公司。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String mcAirline;

    /**
     * 市场方航班号。
     * <p>类型：int；约束：O（可选）；基数：0-1；示例：\d&#123;3,4&#125;3-4位数字。</p>
     */
    private Integer mcFlightNumber;
}
