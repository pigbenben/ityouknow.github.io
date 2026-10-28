package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 税费信息。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 taxes。</p>
 */
@Getter
@Setter
public class Taxes {

    /**
     * 货币单位。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String currency;

    /**
     * 税项。
     * <p>类型：tax；约束：M（必填）；基数：1。</p>
     */
    private Tax tax;

    /**
     * 税项总和。
     * <p>类型：string；约束：M（必填）；基数：1；示例：3位货币代码+金额数字。</p>
     */
    private String totalTaxAmount;
}
