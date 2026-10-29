package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 附加行李信息。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 addlBaggageInfo。</p>
 */
@Getter
@Setter
public class AddlBaggageInfo {

    /**
     * 值机行李件数。
     * <p>类型：int；约束：M（必填）；基数：1。</p>
     */
    private Integer acceptedBaggageCount;

    /**
     * 行李描述信息。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String baggageRemark;

    /**
     * 免费行李额。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String freeBaggageAllowance;
}
