package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 旅客信息。
 */
@Getter
@Setter
public class Traveller {

    /**
     * 婴儿不占座标识。Boolean 转为 String。
     */
    private String infInd;

    /**
     * 姓名中英文标识。
     */
    private Integer nameType;

    /**
     * 姓。
     */
    private String lastName;

    /**
     * 名。
     */
    private String firstName;

    /**
     * 旅客姓名全称。
     */
    private String fullName;

    /**
     * 旅客类型。
     */
    private String passengerType;

}
