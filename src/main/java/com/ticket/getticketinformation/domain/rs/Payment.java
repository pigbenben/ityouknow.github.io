package com.ticket.getticketinformation.domain.rs;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 付款方式信息。
 */
@Getter
@Setter
public class Payment {

    /**
     * 支付形式。
     */
    private FormOfPayment formOfPayment;

    /**
     * 付款信息列表。对应报文 payment 列表。
     */
    private List<SubPayment> subPayment;

}
