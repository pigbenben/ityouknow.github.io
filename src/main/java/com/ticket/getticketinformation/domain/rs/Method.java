package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 付款方式。
 */
@Getter
@Setter
public class Method {

    /**
     * 信用卡付款方式。
     */
    private PaymentCard paymentCard;

    /**
     * 现金支付。
     */
    private Cash cash;

    /**
     * 支票支付方式。
     */
    private Cheque cheque;

    /**
     * 其他付款方式。
     */
    private Other other;

}
