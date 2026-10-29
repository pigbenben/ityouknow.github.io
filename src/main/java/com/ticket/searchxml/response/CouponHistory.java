package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 票联历史。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 couponHistory。</p>
 */
@Getter
@Setter
public class CouponHistory {

    /**
     * agent号。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：nnnnn。</p>
     */
    private String agent;

    /**
     * 到达数据。
     * <p>类型：arrivalData；约束：O（可选）；基数：0-1。</p>
     */
    private ArrivalData arrivalData;

    /**
     * 创建日期。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：YYYY-MM-DD。</p>
     */
    private String createDate;

    /**
     * 创建时间。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：HH:MM。</p>
     */
    private String createTime;

    /**
     * CRS旅客姓名记录。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：[A-Z]&#123;2&#125;[A-Z0-9]&#123;4&#125;。</p>
     */
    private String crsPnr;

    /**
     * 离港数据。
     * <p>类型：departureData；约束：O（可选）；基数：0-1。</p>
     */
    private DepartureData departureData;

    /**
     * ICS旅客姓名记录。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：[A-Z]&#123;2&#125;[A-Z0-9]&#123;4&#125;。</p>
     */
    private String icsPnr;

    /**
     * 市场方承运信息。
     * <p>类型：marketingCarrierInfo；约束：O（可选）；基数：0-1。</p>
     */
    private MarketingCarrierInfo marketingCarrierInfo;

    /**
     * 销售订座状态代码。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String reservationStatusCode;

    /**
     * 序列号。
     * <p>类型：int；约束：O（可选）；基数：0-1。</p>
     */
    private Integer sequenceNumber;
}
