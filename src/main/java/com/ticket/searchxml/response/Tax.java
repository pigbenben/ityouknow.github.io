package com.ticket.searchxml.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 税项。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 tax。</p>
 */
@Getter
@Setter
public class Tax {

    /**
     * 税项明细。
     * <p>类型：subtax；约束：M（必填）；基数：1-N（List）。</p>
     */
    private List<Subtax> subtax;
}
