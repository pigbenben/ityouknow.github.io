package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 原始出票信息。
 */
@Getter
@Setter
public class OriginalIssueInfo {

    /**
     * 原始出票人。
     */
    private String agent;

    /**
     * 原始出票日期，格式 YYYYMMDD。
     */
    private String ticketingDate;

    /**
     * 原始出票地。
     */
    private String cityCode;

    /**
     * 旧票票号。
     */
    private String ticketNumber;

}
