package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 预定信息。
 */
@Getter
@Setter
public class BookingReference {

    /**
     * 国际国内标识。
     */
    private String internationalInd;

    /**
     * 电子客票分类。
     */
    private Integer etType;

    /**
     * 政府采购票标识。
     */
    private Boolean governmentFareInd;

}
