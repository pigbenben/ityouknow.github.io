package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 按票号查询电子客票响应。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 SearchXmlTicketByTicketNumberRS。</p>
 */
@Getter
@Setter
public class SearchXmlTicketByTicketNumberRS {

    /**
     * 请求ID。
     * <p>原字段名：RequestID；XML 属性；类型：string；约束：M（必填）；基数：1；示例：a。</p>
     */
    private String requestID;

    /**
     * SessionID。
     * <p>原字段名：SessionID；XML 属性；类型：string；约束：M（必填）；基数：1；示例：a。</p>
     */
    private String sessionID;

    /**
     * 跟踪带ID。
     * <p>原字段名：TransactionID；XML 属性；类型：string；约束：M（必填）；基数：1；示例：a。</p>
     */
    private String transactionID;

    /**
     * 版本。
     * <p>原字段名：Version；XML 属性；类型：string；约束：M（必填）；基数：1；示例：1.0。</p>
     */
    private String version;

    /**
     * 后台响应。
     * <p>原字段名：Response；类型：Response；约束：M（必填）；基数：1。</p>
     */
    private Response response;

    /**
     * 成功标识。
     * <p>原字段名：Success；类型：string；约束：M（必填）；基数：1；示例：a。</p>
     */
    private String success;
}
