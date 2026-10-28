package com.ticket.searchxml.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 电子客票查询条件。
 * <p>所属接口：SearchXmlTicketByTicketNumber 请求报文，节点 xmltKQueryInfo。</p>
 */
@Getter
@Setter
public class XmltKQueryInfo {

    /**
     * 分销市场方系统ID。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String channelCode;

    /**
     * EDI标志。
     * <p>类型：boolean；约束：O（可选）；基数：0-1；示例：True/False。</p>
     */
    private Boolean ediInd;

    /**
     * 附加查询值。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String extraDisplayValue;

    /**
     * 选项。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String option;

    /**
     * 票号。
     * <p>类型：string；约束：M（必填）；基数：1；示例：\d&#123;1,13&#125; 数字。</p>
     */
    private String ticketNumber;
}
