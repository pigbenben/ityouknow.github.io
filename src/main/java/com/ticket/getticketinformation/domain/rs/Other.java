package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 其他付款方式。
 */
@Getter
@Setter
public class Other {

    /**
     * 其他支付方式备注。由报文 paymentTypeCode 对应生成。
     */
    private OtherPaymentMethodRemarks otherPaymentMethodRemarks;

}
