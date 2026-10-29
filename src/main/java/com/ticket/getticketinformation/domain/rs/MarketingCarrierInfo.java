package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 市场方信息。
 */
@Getter
@Setter
public class MarketingCarrierInfo {

    /**
     * 市场方航空公司。
     */
    private String mcAirline;

    /**
     * 航班号。
     */
    private String mcFlightNumber;

    /**
     * 航班号后缀。
     */
    private String mcFlightSuffix;

    /**
     * 舱位。票联历史节点不填。
     */
    private String mcClassId;

}
