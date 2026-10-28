package com.ticket.searchxml.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 电子客票查询请求。
 * <p>所属接口：SearchXmlTicketByTicketNumber 请求报文，节点 xmlticketRequest。</p>
 */
@Getter
@Setter
public class XmlticketRequest {

    /**
     * 电子客票查询。
     * <p>类型：xmltKQuery；约束：M（必填）；基数：1。</p>
     */
    private XmltKQuery xmltKQuery;
}
