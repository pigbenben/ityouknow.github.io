package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 按票号查询客票信息响应。
 */
@Getter
@Setter
public class GetTicketInformationByTicketNumberResponse {

    /**
     * 服务访问成功的结果。
     */
    private Response response;

}
