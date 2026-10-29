package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 票联历史。
 */
@Getter
@Setter
public class CouponHistory {

    /**
     * 客票历史中的序列号。
     */
    private Integer sequenceNumber;

    /**
     * 修改日期。
     */
    private String createDate;

    /**
     * 修改时间。
     */
    private String createTime;

    /**
     * 操作人。
     */
    private String agent;

    /**
     * 起始信息。
     */
    private Departure departure;

    /**
     * 到达信息。
     */
    private Arrival arrival;

    /**
     * 市场航班代码。
     */
    private MarketingCarrierInfo marketingCarrierInfo;

    /**
     * 预定状态。
     */
    private String reservationStatusCode;

    /**
     * 旅客的 ICS 记录编号。
     */
    private String icsPnr;

    /**
     * CRS 旅客姓名记录。
     */
    private String crsPnr;

}
