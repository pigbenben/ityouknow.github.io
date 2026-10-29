package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 费用信息。
 */
@Getter
@Setter
public class FareInfo {

    /**
     * 票面价信息。
     */
    private FareAmount fareAmount;

    /**
     * 实际票面价。
     */
    private SellAmount sellAmount;

    /**
     * 等值运价信息。
     */
    private EquivFare equivFare;

    /**
     * 票面总价信息。
     */
    private TotalAmount totalAmount;

    /**
     * 税费信息。
     */
    private Taxes taxes;

    /**
     * 运价计算信息。
     */
    private UnstructuredFareCalcInfo unstructuredFareCalcInfo;

}
