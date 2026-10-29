package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 航段有效期信息。
 */
@Getter
@Setter
public class CouponValid {

    /**
     * 航段有效期某时间之前，格式 YYYYMMDD。
     */
    private String notValidBefore;

    /**
     * 航段有效期某时间之后，格式 YYYYMMDD。
     */
    private String notValidAfter;

}
