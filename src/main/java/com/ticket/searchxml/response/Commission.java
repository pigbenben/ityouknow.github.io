package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 代理费信息。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 commission。</p>
 */
@Getter
@Setter
public class Commission {

    /**
     * 代理费。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String commissionAmount;

    /**
     * 定额代理费信息。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String commissionAmountText;

    /**
     * 代理费率。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：6(2)。</p>
     */
    private String commissionPercent;

    /**
     * 佣金类型。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String commissionType;

    /**
     * 货币单位。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String currency;
}
