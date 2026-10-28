package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 订座引用。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 bookingReference。</p>
 */
@Getter
@Setter
public class BookingReference {

    /**
     * 电子客票分类。
     * <p>类型：int；约束：O（可选）；基数：0-1。</p>
     */
    private Integer etType;

    /**
     * 政府采购运价标识。
     * <p>类型：boolean；约束：O（可选）；基数：0-1。</p>
     */
    private Boolean governmentFareInd;

    /**
     * 查验单号。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：\d&#123;n&#125;。</p>
     */
    private String gpDetailNumber;

    /**
     * 国内国际标志。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：I|D。</p>
     */
    private String international;

    /**
     * 支付渠道。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：a。</p>
     */
    private String paymentChannel;
}
