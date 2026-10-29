package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 票面总价信息。
 */
@Getter
@Setter
public class TotalAmount {

    /**
     * 票面总价，保留两位小数。
     */
    private String fareBasis;

    /**
     * 票面总价货币代码。
     */
    private String currency;

    /**
     * 票价类型。
     */
    private String amountType;

    /**
     * 票面价类型。
     */
    private String paymentTypeCode;

}
