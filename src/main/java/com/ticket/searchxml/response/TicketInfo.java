package com.ticket.searchxml.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 客票信息。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 ticketInfo。</p>
 */
@Getter
@Setter
public class TicketInfo {

    /**
     * 子客票信息。
     * <p>类型：subticketInfo；约束：O（可选）；基数：0-N（List）。</p>
     */
    private List<SubticketInfo> subticketInfo;
}
