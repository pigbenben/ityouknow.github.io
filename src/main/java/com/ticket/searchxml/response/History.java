package com.ticket.searchxml.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 历史记录。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 history。</p>
 */
@Getter
@Setter
public class History {

    /**
     * 历史记录明细。
     * <p>类型：subhistory；约束：O（可选）；基数：0-N（List）。</p>
     */
    private List<Subhistory> subhistory;
}
