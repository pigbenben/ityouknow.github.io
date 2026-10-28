package com.ticket.searchxml.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 身份证明。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 formIDs。</p>
 */
@Getter
@Setter
public class FormIDs {

    /**
     * 身份证明明细。
     * <p>类型：subformIDs；约束：O（可选）；基数：0-N（List）。</p>
     */
    private List<SubformIDs> subformIDs;
}
