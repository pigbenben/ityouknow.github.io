package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 票面价信息。
 */
@Getter
@Setter
public class FareAmount {

    /**
     * 票面价，保留两位小数。
     */
    private String fareBasis;

    /**
     * 票面价货币代码。支付类型为 F 或 R 且票面价不为空时才返回。
     */
    private String currency;

    /**
     * 票面价类型。
     */
    private String paymentTypeCode;

}
