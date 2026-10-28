package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 起飞航站楼信息。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 depTerminalInfo。</p>
 */
@Getter
@Setter
public class DepTerminalInfo {

    /**
     * 登机号。
     * <p>类型：int；约束：O（可选）；基数：0-1。</p>
     */
    private Integer boardingNumber;

    /**
     * 起飞航站楼。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：[A-Z\d]&#123;5&#125; 字母或数字。</p>
     */
    private String departureTerminal;
}
