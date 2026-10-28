package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 票联有效期。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 couponValid。</p>
 */
@Getter
@Setter
public class CouponValid {

    /**
     * 失效日期。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：YYYY-MM-DD。</p>
     */
    private String expiryDate;

    /**
     * 运价中的B项。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String notValidBefore;
}
