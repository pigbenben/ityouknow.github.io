package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 常旅客账户。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 account。</p>
 */
@Getter
@Setter
public class Account {

    /**
     * 常旅客卡号。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String frequentFlyerNumber;
}
