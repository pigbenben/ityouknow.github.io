package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 基础运价。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 baseFare。</p>
 */
@Getter
@Setter
public class BaseFare {

    /**
     * 货币单位。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String currency;

    /**
     * 运价基础代码。
     * <p>类型：string；约束：M（必填）；基数：1；示例：[A-Z][A-Z\d]&#123;,11&#125; 字母+数字，第一位不能是数字，支持 /。</p>
     */
    private String fareBasis;

    /**
     * 支付类型。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：[A-Z]&#123;2&#125;。</p>
     */
    private String paymentTypeCode;
}
