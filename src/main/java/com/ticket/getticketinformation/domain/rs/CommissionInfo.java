package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 代理费信息。
 */
@Getter
@Setter
public class CommissionInfo {

    /**
     * 代理费。
     */
    private String commissionAmount;

    /**
     * 代理费类型。
     */
    private String commissionType;

    /**
     * 货币代码。
     */
    private String currency;

}
