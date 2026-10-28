package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 代理人身份。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 agentIdentity。</p>
 */
@Getter
@Setter
public class AgentIdentity {

    /**
     * 公司ID。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String corporationId;

    /**
     * 销售打票机编号。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String deviceNum;

    /**
     * 集团号。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String holdingCompanyId;

    /**
     * IATA号。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String iataNumber;

    /**
     * OFFICE代码。
     * <p>类型：string；约束：M（必填）；基数：1；示例：aaannn。</p>
     */
    private String office;

    /**
     * 操作代理人。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String operationAgentId;

    /**
     * 服务组代码。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String serviceGroupCode;
}
