package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 航段状态信息。
 */
@Getter
@Setter
public class Status {

    /**
     * 航段状态。
     */
    private String couponStatusCode;

    /**
     * 控制权航空公司代码。
     */
    private String airportControlAirline;

}
