package com.ticket.searchxml.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 常旅客信息。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 fQTV。</p>
 */
@Getter
@Setter
public class FQTV {

    /**
     * 常旅客明细。
     * <p>类型：subfQTV；约束：O（可选）；基数：0-N（List）。</p>
     */
    private List<SubfQTV> subfQTV;
}
