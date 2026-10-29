package com.ticket.searchxml.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 支付信息。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 payments。</p>
 */
@Getter
@Setter
public class Payments {

    /**
     * 支付形式。
     * <p>类型：formOfPayment；约束：M（必填）；基数：1。</p>
     */
    private FormOfPayment formOfPayment;

    /**
     * 支付明细列表。
     * <p>类型：payment；约束：M（必填）；基数：1-N（List）。subpayment 的属性已上收到 Payment。</p>
     */
    private List<Payment> payment;

    /**
     * 出示信用卡标识。
     * <p>类型：boolean；约束：O（可选）；基数：0-1。</p>
     */
    private Boolean presentCreditCardInd;
}
