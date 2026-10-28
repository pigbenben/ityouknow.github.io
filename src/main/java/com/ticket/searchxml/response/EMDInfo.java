package com.ticket.searchxml.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * EMD 信息。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 eMDInfo。</p>
 */
@Getter
@Setter
public class EMDInfo {

    /**
     * EMD 明细。
     * <p>类型：subeMDInfo；约束：O（可选）；基数：0-N（List）。</p>
     */
    private List<SubeMDInfo> subeMDInfo;
}
