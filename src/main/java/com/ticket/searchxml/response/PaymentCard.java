package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 支付卡。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 paymentCard。</p>
 */
@Getter
@Setter
public class PaymentCard {

    /**
     * 授权代码。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：1。</p>
     */
    private String approvalCode;

    /**
     * 发卡方。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String cardCode;

    /**
     * 支付卡号。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String cardNo;

    /**
     * 失效日期。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：1。</p>
     */
    private String expiryDate;
}
