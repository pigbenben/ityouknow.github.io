package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * EMD 明细。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 subeMDInfo。</p>
 */
@Getter
@Setter
public class SubeMDInfo {

    /**
     * 票联号。
     * <p>类型：int；约束：M（必填）；基数：1；示例：\d 数字。</p>
     */
    private Integer couponNumber;

    /**
     * 关联标识。字段名沿用接口文档原文拼写 isAccosicated。
     * <p>类型：boolean；约束：M（必填）；基数：1。</p>
     */
    private Boolean isAccosicated;

    /**
     * 票证类型。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String ticketDocTypeCode;

    /**
     * 票号。
     * <p>类型：string；约束：M（必填）；基数：1；示例：\d&#123;1,13&#125; 数字。</p>
     */
    private String ticketNumber;
}
