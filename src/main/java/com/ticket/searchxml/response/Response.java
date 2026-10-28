package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 后台响应。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 Response。</p>
 */
@Getter
@Setter
public class Response {

    /**
     * 查询结果。
     * <p>类型：result；约束：M（必填）；基数：1。</p>
     */
    private Result result;
}
