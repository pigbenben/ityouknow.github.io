package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 出票备注。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 issueRemark。</p>
 */
@Getter
@Setter
public class IssueRemark {

    /**
     * 文本。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String text;
}
