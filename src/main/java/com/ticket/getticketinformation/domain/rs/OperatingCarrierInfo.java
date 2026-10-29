package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 承运方信息。
 */
@Getter
@Setter
public class OperatingCarrierInfo {

    /**
     * 航空公司。
     */
    private String ocAirline;

    /**
     * 航班号。
     */
    private String ocFlightNumber;

    /**
     * 承运航班号后缀。
     */
    private String ocFlightSuffix;

    /**
     * 舱位。
     */
    private String ocClassId;

}
