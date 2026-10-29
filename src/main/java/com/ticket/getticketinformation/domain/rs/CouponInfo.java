package com.ticket.getticketinformation.domain.rs;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 航段信息。
 */
@Getter
@Setter
public class CouponInfo {

    /**
     * 航段序列号。
     */
    private String segmentId;

    /**
     * 航段号。
     */
    private Integer couponNumber;

    /**
     * ICS 系统编号。
     */
    private String icsPnr;

    /**
     * CRS 系统编号。
     */
    private String crsPnr;

    /**
     * 销售系统代码。
     */
    private String crsCode;

    /**
     * 票价基础信息。
     */
    private String fareBasis;

    /**
     * 航段有效期信息。
     */
    private CouponValid couponValid;

    /**
     * 航段状态信息。
     */
    private Status status;

    /**
     * 离港信息。
     */
    private Departure departure;

    /**
     * 到港信息。
     */
    private Arrival arrival;

    /**
     * 航班日期跨度和起飞日期的差值。
     */
    private String departureDayChange;

    /**
     * 航班到达日期与起飞日期的差值。
     */
    private String arrivalDayChange;

    /**
     * 承运方信息，仅代码共享时存在。
     */
    private OperatingCarrierInfo operatingCarrierInfo;

    /**
     * 舱位。优先 classId，否则取市场方舱位 mcRbd。
     */
    private String classId;

    /**
     * 市场方信息。
     */
    private MarketingCarrierInfo marketingCarrierInfo;

    /**
     * 订座状态信息。
     */
    private String reservationStatusCode;

    /**
     * 携带行李信息。
     */
    private BaggageInfo baggageInfo;

    /**
     * 转机点标识。
     */
    private Boolean stopOverInd;

    /**
     * 是否 E-Trip。
     */
    private Boolean easyTripInd;

    /**
     * EMD 信息列表。
     */
    private List<EMDInfo> emdInfo;

    /**
     * MCO 信息。
     */
    private String mcoInfo;

    /**
     * 南航标准中的非自愿标识。
     */
    private Integer stpsType;

    /**
     * NOSHOW 标识。
     */
    private Integer noshowType;

    /**
     * 非自愿 IRR 标识。
     */
    private Integer veidType;

    /**
     * 非自愿 INVL 标识。
     */
    private Integer invlType;

    /**
     * 改期次数。
     */
    private Integer reissueCount;

    /**
     * 票联历史。报文为单个节点时包装为列表。
     */
    private List<CouponHistory> couponHistory;

}
