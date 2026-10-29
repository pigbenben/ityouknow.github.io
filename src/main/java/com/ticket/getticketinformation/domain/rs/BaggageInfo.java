package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 携带行李信息。
 */
@Getter
@Setter
public class BaggageInfo {

    /**
     * 允许免费行李额度。
     */
    private String freeBaggageAllowance;

}
