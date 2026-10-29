package com.ticket.getticketinformation.domain.rs;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 税费信息。
 */
@Getter
@Setter
public class Taxes {

    /**
     * 税费信息列表。
     */
    private List<Tax> taxList;

    /**
     * 总税费。由金额字符串拆分货币码和数值。
     */
    private TotalTaxe totalTaxAmount;

}
