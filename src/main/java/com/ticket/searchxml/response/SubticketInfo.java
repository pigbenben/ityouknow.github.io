package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 子客票信息。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 subticketInfo。</p>
 */
@Getter
@Setter
public class SubticketInfo {

    /**
     * 代理人标识。
     * <p>类型：agentIDs；约束：O（可选）；基数：0-1。</p>
     */
    private AgentIDs agentIDs;

    /**
     * 订座引用。
     * <p>类型：bookingReference；约束：M（必填）；基数：1。</p>
     */
    private BookingReference bookingReference;

    /**
     * 代理费信息。
     * <p>类型：commission；约束：O（可选）；基数：0-1。</p>
     */
    private Commission commission;

    /**
     * 文本。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String endorsementInformation;

    /**
     * 运价信息。
     * <p>类型：fareInfo；约束：O（可选）；基数：0-1。</p>
     */
    private FareInfo fareInfo;

    /**
     * 出票航空公司信息。
     * <p>类型：issuingAirlineInfo；约束：M（必填）；基数：1。</p>
     */
    private IssuingAirlineInfo issuingAirlineInfo;

    /**
     * 不可签转标识。
     * <p>类型：boolean；约束：O（可选）；基数：0-1。</p>
     */
    private Boolean nonEndorsableInd;

    /**
     * 始发到达城市。
     * <p>类型：originDestination；约束：O（可选）；基数：0-1。</p>
     */
    private OriginDestination originDestination;

    /**
     * 原票出票信息。
     * <p>类型：originalIssueInfo；约束：O（可选）；基数：0-1。</p>
     */
    private OriginalIssueInfo originalIssueInfo;

    /**
     * 支付信息。
     * <p>类型：payments；约束：O（可选）；基数：0-1。</p>
     */
    private Payments payments;

    /**
     * 客票单据信息。
     * <p>类型：ticketDocumentInfo；约束：M（必填）；基数：1。</p>
     */
    private TicketDocumentInfo ticketDocumentInfo;

    /**
     * 旅客信息。
     * <p>类型：traveller；约束：M（必填）；基数：1。</p>
     */
    private Traveller traveller;
}
