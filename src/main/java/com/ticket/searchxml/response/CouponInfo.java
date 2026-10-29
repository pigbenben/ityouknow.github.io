package com.ticket.searchxml.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 票联信息。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 couponInfo。原 sub 节点已去掉，其属性上收到本类。</p>
 */
@Getter
@Setter
public class CouponInfo {

    /**
     * 附加行李信息。
     * <p>类型：addlBaggageInfo；约束：O（可选）；基数：0-1。</p>
     */
    private AddlBaggageInfo addlBaggageInfo;

    /**
     * 航空公司。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String airlineCode;

    /**
     * 到达状态不明航段标识。
     * <p>类型：boolean；约束：O（可选）；基数：0-1。</p>
     */
    private Boolean arnkInd;

    /**
     * 到达信息。
     * <p>类型：arrival；约束：M（必填）；基数：1。</p>
     */
    private Arrival arrival;

    /**
     * 到达日期偏移量。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String arrivalDayChange;

    /**
     * 销售舱位代码。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：[A-Z] 字母。</p>
     */
    private String classId;

    /**
     * 票联历史。
     * <p>类型：couponHistory；约束：O（可选）；基数：0-1。</p>
     */
    private CouponHistory couponHistory;

    /**
     * 票联号。
     * <p>类型：int；约束：M（必填）；基数：1；示例：\d 数字。</p>
     */
    private Integer couponNumber;

    /**
     * 票联有效期。
     * <p>类型：couponValid；约束：O（可选）；基数：0-1。</p>
     */
    private CouponValid couponValid;

    /**
     * CRS系统标识。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String crsCode;

    /**
     * CRS旅客姓名记录。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：[A-Z]&#123;2&#125;[A-Z0-9]&#123;4&#125;。</p>
     */
    private String crsPnr;

    /**
     * 离港信息。
     * <p>类型：departure；约束：M（必填）；基数：1。</p>
     */
    private Departure departure;

    /**
     * 起飞日期偏移量。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String departureDayChange;

    /**
     * EMD 信息。
     * <p>类型：eMDInfo；约束：O（可选）；基数：0-N（List）。subeMDInfo 的属性已上收到 EMDInfo。</p>
     */
    private List<EMDInfo> eMDInfo;

    /**
     * 是否E-Trip。
     * <p>类型：boolean；约束：O（可选）；基数：0-1。</p>
     */
    private Boolean easyTripInd;

    /**
     * 机型。
     * <p>类型：equipment；约束：O（可选）；基数：0-1。</p>
     */
    private Equipment equipment;

    /**
     * 运价基础代码。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：[A-Z][A-Z\d]&#123;,11&#125; 字母+数字，第一位不能是数字，支持 /。</p>
     */
    private String fareBasis;

    /**
     * 类型。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String flightSegmentType;

    /**
     * ICS旅客姓名记录。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：[A-Z]&#123;2&#125;[A-Z0-9]&#123;4&#125;。</p>
     */
    private String icsPnr;

    /**
     * 非自愿标识(INVL)0,1,2。
     * <p>类型：int；约束：O（可选）；基数：0-1。</p>
     */
    private Integer invlType;

    /**
     * 转机点标识。
     * <p>类型：boolean；约束：O（可选）；基数：0-1。</p>
     */
    private Boolean isStopover;

    /**
     * 市场方航班数据。
     * <p>类型：marketingCarrierData；约束：M（必填）；基数：1。</p>
     */
    private MarketingCarrierData marketingCarrierData;

    /**
     * MCO信息。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String mcoInfo;

    /**
     * NOSHOW标识。
     * <p>类型：int；约束：O（可选）；基数：0-1；示例：0。</p>
     */
    private Integer noshowType;

    /**
     * 承运方航班数据。
     * <p>类型：operatingCarrier；约束：O（可选）；基数：0-1。</p>
     */
    private OperatingCarrier operatingCarrier;

    /**
     * 销售订座状态代码。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String reservationStatusCode;

    /**
     * 改期次数。
     * <p>类型：int；约束：M（必填）；基数：1。</p>
     */
    private Integer revalidationTimes;

    /**
     * 座位号。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：\d&#123;1,4&#125;[A-Z] 一般用行号（最多4位数字）+列号（一位大写字母）的方式表达。</p>
     */
    private String seatNumber;

    /**
     * 航班序号。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String segmentId;

    /**
     * 结算授权码SAC。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String settlementAuthCode;

    /**
     * 操作状态。
     * <p>类型：status；约束：M（必填）；基数：1。</p>
     */
    private Status status;

    /**
     * 南航标准中的非自愿标识。
     * <p>类型：int；约束：O（可选）；基数：0-1。</p>
     */
    private Integer stpsType;

    /**
     * 时差。
     * <p>类型：timeDifference；约束：O（可选）；基数：0-1。</p>
     */
    private TimeDifference timeDifference;

    /**
     * 非自愿IRR标识(VEID)。
     * <p>类型：int；约束：O（可选）；基数：0-1。</p>
     */
    private Integer veidType;
}
