package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 税项明细。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 subtax。</p>
 */
@Getter
@Setter
public class Subtax {

    /**
     * 金额。
     * <p>类型：string；约束：M（必填）；基数：1；示例：3位货币代码+金额数字。</p>
     */
    private String amount;

    /**
     * 货币单位。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String currency;

    /**
     * 货币。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String currencyCode;

    /**
     * 税代码。
     * <p>类型：string；约束：M（必填）；基数：1；示例：[A-Z]&#123;,2&#125; 大写字母。</p>
     */
    private String taxCode;

    /**
     * 税类型。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String taxTypes;
}
