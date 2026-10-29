package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 身份信息。
 */
@Getter
@Setter
public class IdentityInfo {

    /**
     * 身份证明类型。
     */
    private String foidTypeCode;

    /**
     * 身份证明号码。
     */
    private String foidNumber;

}
