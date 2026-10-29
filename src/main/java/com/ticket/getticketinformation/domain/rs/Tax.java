package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 税费明细。
 */
@Getter
@Setter
public class Tax {

    /**
     * 税费。
     */
    private String amount;

    /**
     * 税费货币代码。
     */
    private String currency;

    /**
     * 税务码。
     */
    private String taxCode;

    /**
     * 新税旧税标识。
     */
    private String oldOrNewTaxInd;

}
