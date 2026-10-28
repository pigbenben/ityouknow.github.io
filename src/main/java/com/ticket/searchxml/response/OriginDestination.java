package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 始发到达城市。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 originDestination。</p>
 */
@Getter
@Setter
public class OriginDestination {

    /**
     * 到达城市。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String arrivalCityCode;

    /**
     * 起飞城市。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String departureCityCode;
}
