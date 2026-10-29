package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 离港信息。
 */
@Getter
@Setter
public class Departure {

    /**
     * 航段始发地。
     */
    private String departureAirport;

    /**
     * 离港日期，格式 YYYYMMDD。
     */
    private String departureDate;

    /**
     * 航班离港时间，格式 HHmm。
     */
    private String departureTime;

    /**
     * 航站楼。票联历史节点不填。
     */
    private Terminal terminal;

}
