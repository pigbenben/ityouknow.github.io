package com.ticket.getticketinformation.domain.rs;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 票面基本信息。
 */
@Getter
@Setter
public class TicketDocumentInfo {

    /**
     * 行程单打印标识。来源为空时为 false。
     */
    private Boolean receiptPrintedInd;

    /**
     * 是否有保险。
     */
    private Boolean insuranceInd;

    /**
     * 票号。
     */
    private String ticketNumber;

    /**
     * 联票总数。
     */
    private Integer conjunctionTotalTicket;

    /**
     * 联票序号。
     */
    private Integer conjunctionRelativeTicket;

    /**
     * 出票日期，格式 YYYYMMDD。
     */
    private String issueDate;

    /**
     * 出票时间，格式 HHmm。
     */
    private String issueTime;

    /**
     * 出票地三字码。
     */
    private String issueCityCode;

    /**
     * 编号。
     */
    private String iataNumber;

    /**
     * 备注。
     */
    private String remark;

    /**
     * 航段信息列表。
     */
    private List<CouponInfo> couponInfo;

    /**
     * 旅行代码信息。
     */
    private String tourCode;

    /**
     * 身份信息列表。
     */
    private List<IdentityInfo> identityInfo;

    /**
     * 打票机类型。
     */
    private String deviceTypeCode;

}
