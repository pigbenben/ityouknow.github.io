package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 运价信息。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 fareInfo。</p>
 */
@Getter
@Setter
public class FareInfo {

    /**
     * 实际基础运价。
     * <p>类型：actualBaseFare；约束：O（可选）；基数：0-1。</p>
     */
    private ActualBaseFare actualBaseFare;

    /**
     * 实际总运价。
     * <p>类型：actualTotalFare；约束：O（可选）；基数：0-1。</p>
     */
    private ActualTotalFare actualTotalFare;

    /**
     * 基础运价。
     * <p>类型：baseFare；约束：M（必填）；基数：1。</p>
     */
    private BaseFare baseFare;

    /**
     * 等价运价。
     * <p>类型：equivFare；约束：O（可选）；基数：0-1。</p>
     */
    private EquivFare equivFare;

    /**
     * 税费信息。
     * <p>类型：taxes；约束：O（可选）；基数：0-1。</p>
     */
    private Taxes taxes;

    /**
     * 总运价。
     * <p>类型：totalFare；约束：O（可选）；基数：0-1。</p>
     */
    private TotalFare totalFare;

    /**
     * 非结构化运价计算信息。
     * <p>类型：unstructuredFareCalcInfo；约束：O（可选）；基数：0-1。</p>
     */
    private UnstructuredFareCalcInfo unstructuredFareCalcInfo;
}
