package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 信用卡付款方式。
 */
@Getter
@Setter
public class PaymentCard {

    /**
     * 发卡方。
     */
    private String cardCode;

    /**
     * 信用卡号。
     */
    private String cardNo;

}
