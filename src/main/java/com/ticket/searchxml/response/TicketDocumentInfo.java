package com.ticket.searchxml.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 客票单据信息。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 ticketDocumentInfo。</p>
 */
@Getter
@Setter
public class TicketDocumentInfo {

    /**
     * 客票单据明细。
     * <p>类型：subticketDocumentInfo；约束：M（必填）；基数：1-N（List）。</p>
     */
    private List<SubticketDocumentInfo> subticketDocumentInfo;
}
