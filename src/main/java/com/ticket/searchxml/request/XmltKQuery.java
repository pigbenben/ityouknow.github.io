package com.ticket.searchxml.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 电子客票查询。
 * <p>所属接口：SearchXmlTicketByTicketNumber 请求报文，节点 xmltKQuery。</p>
 */
@Getter
@Setter
public class XmltKQuery {

    /**
     * 电子客票查询条件。
     * <p>类型：xmltKQueryInfo；约束：M（必填）；基数：1。</p>
     */
    private XmltKQueryInfo xmltKQueryInfo;
}
