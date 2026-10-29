package com.ticket.getticketinformation.domain.rq;

import lombok.Getter;
import lombok.Setter;

/**
 * 位置信息。
 */
@Getter
@Setter
public class LocationInfo {

    /**
     * 客户编号。报文无对应节点，不参与转换。
     */
    private String customerID;

}
