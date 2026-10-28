package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 到达数据。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 arrivalData。</p>
 */
@Getter
@Setter
public class ArrivalData {

    /**
     * 目的地代码（机场或城市）。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：aaa。</p>
     */
    private String destination;
}
