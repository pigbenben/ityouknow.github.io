package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 历史记录明细。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 subhistory。</p>
 */
@Getter
@Setter
public class Subhistory {

    /**
     * agent号。
     * <p>类型：string；约束：M（必填）；基数：1；示例：nnnnn。</p>
     */
    private String agent;

    /**
     * 票联号。
     * <p>类型：int；约束：M（必填）；基数：1；示例：\d 数字。</p>
     */
    private Integer couponNumber;

    /**
     * 创建日期。
     * <p>类型：string；约束：M（必填）；基数：1；示例：YYYY-MM-DD。</p>
     */
    private String createDate;

    /**
     * 创建时间。
     * <p>类型：string；约束：M（必填）；基数：1；示例：HH:MM。</p>
     */
    private String createTime;

    /**
     * 历史记录删除标识。
     * <p>类型：boolean；约束：O（可选）；基数：0-1。</p>
     */
    private Boolean deleteIndicator;

    /**
     * 备注类型。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String remarkTypeCode;

    /**
     * 序列号。
     * <p>类型：int；约束：M（必填）；基数：1。</p>
     */
    private Integer sequenceNumber;

    /**
     * 文本。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String text;
}
