package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 到港信息。
 */
@Getter
@Setter
public class Arrival {

    /**
     * 航班到达地。
     */
    private String arrivalAirport;

    /**
     * 到达日期，格式 YYYYMMDD。票联历史节点不填。
     */
    private String arrivalDate;

    /**
     * 计划到达时刻，格式 HHmm。票联历史节点不填。
     */
    private String arrivalTime;

    /**
     * 航站楼。票联历史节点不填。
     */
    private Terminal terminal;

}
