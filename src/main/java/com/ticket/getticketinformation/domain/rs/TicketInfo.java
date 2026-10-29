package com.ticket.getticketinformation.domain.rs;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 客票信息。
 */
@Getter
@Setter
public class TicketInfo {

    /**
     * 旅客信息。
     */
    private Traveller traveller;

    /**
     * 票面基本信息列表。
     */
    private List<TicketDocumentInfo> ticketDocumentInfo;

    /**
     * 预定信息。
     */
    private BookingReference bookingReference;

    /**
     * 出票航空公司信息。
     */
    private IssuingAirlineInfo issuingAirlineInfo;

    /**
     * 原始出票信息。
     */
    private OriginalIssueInfo originalIssueInfo;

    /**
     * 起始到达地信息。
     */
    private OriginDestination originDestination;

    /**
     * 费用信息。
     */
    private FareInfo fareInfo;

    /**
     * 代理费信息。
     */
    private CommissionInfo commission;

    /**
     * 付款方式信息。
     */
    private List<Payment> paymentList;

    /**
     * 签注信息。
     */
    private String endorsementInformation;

}
