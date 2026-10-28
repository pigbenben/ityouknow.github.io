package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 查询结果。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 result。</p>
 */
@Getter
@Setter
public class Result {

    /**
     * 错误信息。
     * <p>类型：errorInfo；约束：O（可选）；基数：0-1。</p>
     */
    private ErrorInfo errorInfo;

    /**
     * 客票信息。
     * <p>类型：ticketInfo；约束：O（可选）；基数：0-1。</p>
     */
    private TicketInfo ticketInfo;

    /**
     * 电子客票备注信息。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String ticketRemark;
}
