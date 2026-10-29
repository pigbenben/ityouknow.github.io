package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * EMD 信息。
 */
@Getter
@Setter
public class EMDInfo {

    /**
     * 票号。
     */
    private String ticketNumber;

    /**
     * 航段号。
     */
    private Integer couponNumber;

    /**
     * 票证类型。
     */
    private String ticketDocTypeCode;

    /**
     * 关联标识。
     */
    private Boolean inConnectionInd;

}
