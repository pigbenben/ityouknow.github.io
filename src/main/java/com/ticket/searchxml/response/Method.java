package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 支付方式。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 method。</p>
 */
@Getter
@Setter
public class Method {

    /**
     * 现金支付。
     * <p>类型：cash；约束：O（可选）；基数：0-1。</p>
     */
    private Cash cash;

    /**
     * 支票支付。
     * <p>类型：cheque；约束：O（可选）；基数：0-1。</p>
     */
    private Cheque cheque;

    /**
     * 其他支付方式。
     * <p>类型：other；约束：O（可选）；基数：0-1。</p>
     */
    private Other other;

    /**
     * 支付卡。
     * <p>类型：paymentCard；约束：O（可选）；基数：0-1。</p>
     */
    private PaymentCard paymentCard;
}
