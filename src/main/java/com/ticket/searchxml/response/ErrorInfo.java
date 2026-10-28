package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 错误信息。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 errorInfo。</p>
 */
@Getter
@Setter
public class ErrorInfo {

    /**
     * 错误码。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String errorCode;

    /**
     * 错误详细信息。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String errorMessage;
}
