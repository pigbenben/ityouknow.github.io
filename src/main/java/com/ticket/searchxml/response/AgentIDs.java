package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 代理人标识。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 agentIDs。</p>
 */
@Getter
@Setter
public class AgentIDs {

    /**
     * 代理人身份。
     * <p>类型：agentIdentity；约束：M（必填）；基数：1。</p>
     */
    private AgentIdentity agentIdentity;
}
