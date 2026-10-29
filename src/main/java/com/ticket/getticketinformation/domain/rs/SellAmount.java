package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 实际票面价。
 */
@Getter
@Setter
public class SellAmount {

    /**
     * 实收价，保留两位小数。
     */
    private String fareBasis;

    /**
     * 实收价货币代码。
     */
    private String currency;

    /**
     * 票面价类型。
     */
    private String paymentTypeCode;

}
