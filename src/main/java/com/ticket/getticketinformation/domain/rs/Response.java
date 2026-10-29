package com.ticket.getticketinformation.domain.rs;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 服务访问成功的结果。
 */
@Getter
@Setter
public class Response {

    /**
     * 客票信息列表。
     */
    private List<TicketInfo> ticketInfo;

}
