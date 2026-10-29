package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 总税费。
 */
@Getter
@Setter
public class TotalTaxe {

    /**
     * 税货币码，取总税费字符串前 3 位。
     */
    private String taxCode;

    /**
     * 总税费金额，取总税费字符串去掉前 3 位后的数字。
     */
    private String value;

}
