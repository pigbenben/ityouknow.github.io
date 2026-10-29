package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 出票航空公司信息。
 */
@Getter
@Setter
public class IssuingAirlineInfo {

    /**
     * 航空公司全称。
     */
    private String airlineName;

    /**
     * 出票航空公司代码。
     */
    private String airline;

}
