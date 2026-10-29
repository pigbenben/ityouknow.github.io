package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 非结构化运价计算信息。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 unstructuredFareCalcInfo。</p>
 */
@Getter
@Setter
public class UnstructuredFareCalcInfo {

    /**
     * 自动运价。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String autoFareType;

    /**
     * 文本。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String text;
}
