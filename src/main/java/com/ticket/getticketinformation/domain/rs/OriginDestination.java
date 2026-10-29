package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 起始到达地信息。
 */
@Getter
@Setter
public class OriginDestination {

    /**
     * 起始地三字码。
     */
    private String departureAirport;

    /**
     * 目的地三字码。
     */
    private String arrivalAirport;

}
