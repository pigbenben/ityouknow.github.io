package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 付款信息。
 */
@Getter
@Setter
public class SubPayment {

    /**
     * 付款方式。
     */
    private Method method;

}
