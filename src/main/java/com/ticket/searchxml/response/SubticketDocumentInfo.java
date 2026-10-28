package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 客票单据明细。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 subticketDocumentInfo。</p>
 */
@Getter
@Setter
public class SubticketDocumentInfo {

    /**
     * 能否在机场打印报销凭证。
     * <p>类型：boolean；约束：O（可选）；基数：0-1。</p>
     */
    private Boolean airportReceiptInd;

    /**
     * 联票顺序序号。
     * <p>类型：int；约束：M（必填）；基数：1；示例：\d&#123;,2&#125; 数字。</p>
     */
    private Integer conjunction;

    /**
     * 联票标识。
     * <p>类型：boolean；约束：O（可选）；基数：0-1。</p>
     */
    private Boolean conjunctionTicketInd;

    /**
     * 联票总数。
     * <p>类型：int；约束：M（必填）；基数：1；示例：a1。</p>
     */
    private Integer conjunctionTicketTotalNumber;

    /**
     * 票联信息。
     * <p>类型：couponInfo；约束：O（可选）；基数：0-1。</p>
     */
    private CouponInfo couponInfo;

    /**
     * 打票机类型。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String deviceTypeCode;

    /**
     * 失效日期。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：YYYY-MM-DD。</p>
     */
    private String expiryDate;

    /**
     * 身份证明。
     * <p>类型：formIDs；约束：O（可选）；基数：0-1。</p>
     */
    private FormIDs formIDs;

    /**
     * 历史记录。
     * <p>类型：history；约束：O（可选）；基数：0-1。</p>
     */
    private History history;

    /**
     * IATA号。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String iataNumber;

    /**
     * 保险标识。
     * <p>类型：boolean；约束：O（可选）；基数：0-1。</p>
     */
    private Boolean insuranceInd;

    /**
     * 出票城市代码。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：3!a。</p>
     */
    private String issueCityCode;

    /**
     * 出票备注。
     * <p>类型：issueRemark；约束：O（可选）；基数：0-1。</p>
     */
    private IssueRemark issueRemark;

    /**
     * 出票时间。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：HH:MM。</p>
     */
    private String issueTime;

    /**
     * 行程单号。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String itineraryNumber;

    /**
     * 行程单状态。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String itineraryStatusCode;

    /**
     * 换开标识。
     * <p>类型：boolean；约束：O（可选）；基数：0-1。</p>
     */
    private Boolean reissueInd;

    /**
     * 备注。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String remark;

    /**
     * 票号。
     * <p>类型：string；约束：M（必填）；基数：1；示例：\d&#123;1,13&#125; 数字。</p>
     */
    private String ticketNumber;

    /**
     * 出票日期。
     * <p>类型：string；约束：M（必填）；基数：1；示例：YYYY-MM-DD。</p>
     */
    private String ticketingDate;

    /**
     * 出票地。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：a3。</p>
     */
    private String ticketingLocation;

    /**
     * 旅行编码。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String tourCode;
}
