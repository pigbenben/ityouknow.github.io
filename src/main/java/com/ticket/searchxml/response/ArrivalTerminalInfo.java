package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 到达航站楼信息。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 arrivalTerminalInfo。</p>
 */
@Getter
@Setter
public class ArrivalTerminalInfo {

    /**
     * 到达口。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String arrivalGate;

    /**
     * 到达航站楼。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：[A-Z\d]&#123;5&#125; 字母或数字。</p>
     */
    private String arrivalTerminal;
}
