package com.ticket.searchxml.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 查询请求。
 * <p>所属接口：SearchXmlTicketByTicketNumber 请求报文，节点 Query。</p>
 */
@Getter
@Setter
public class Query {

    /**
     * 电子客票查询请求。
     * <p>类型：xmlticketRequest；约束：M（必填）；基数：1。</p>
     */
    private XmlticketRequest xmlticketRequest;
}
