package com.ticket.searchxml.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 按票号查询电子客票请求。
 * <p>所属接口：SearchXmlTicketByTicketNumber 请求报文，节点 SearchXmlTicketByTicketNumberRQ。</p>
 */
@Getter
@Setter
public class SearchXmlTicketByTicketNumberRQ {

    /**
     * 请求。
     * <p>原字段名：Query；类型：Query；约束：M（必填）；基数：1。</p>
     */
    private Query query;
}
