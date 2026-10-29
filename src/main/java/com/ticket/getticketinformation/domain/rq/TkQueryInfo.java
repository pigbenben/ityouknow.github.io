package com.ticket.getticketinformation.domain.rq;

import lombok.Getter;
import lombok.Setter;

/**
 * 票价查询信息。
 */
@Getter
@Setter
public class TkQueryInfo {

    /**
     * 航司二字码。报文无对应节点，不参与转换。
     */
    private String airline;

    /**
     * 票号。
     */
    private String ticketNumber;

}
