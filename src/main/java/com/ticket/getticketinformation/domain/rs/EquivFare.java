package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 等值运价信息。
 */
@Getter
@Setter
public class EquivFare {

    /**
     * 换算票价，保留两位小数。
     */
    private String fareBasis;

    /**
     * 换算票价货币代码。
     */
    private String currency;

    /**
     * 票面价类型。
     */
    private String paymentTypeCode;

}
