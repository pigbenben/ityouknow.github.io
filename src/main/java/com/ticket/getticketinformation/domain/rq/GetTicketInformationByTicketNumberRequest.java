package com.ticket.getticketinformation.domain.rq;

import lombok.Getter;
import lombok.Setter;

/**
 * 按票号查询客票信息请求。
 */
@Getter
@Setter
public class GetTicketInformationByTicketNumberRequest {

    /**
     * 请求。
     */
    private Query query;

}
