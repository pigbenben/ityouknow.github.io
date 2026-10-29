package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 原票出票信息。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 originalIssueInfo。</p>
 */
@Getter
@Setter
public class OriginalIssueInfo {

    /**
     * agent号。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：nnnnn。</p>
     */
    private String agent;

    /**
     * IATA号。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String iataNumber;

    /**
     * 最后一次换开票号。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：\d&#123;1,13&#125; 数字。</p>
     */
    private String inExchangeFor;

    /**
     * 地理位置英文名称。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String locationEnName;

    /**
     * 票号。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：\d&#123;1,13&#125; 数字。</p>
     */
    private String ticketNumber;

    /**
     * 出票日期。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：YYYY-MM-DD。</p>
     */
    private String ticketingDate;
}
