package com.ticket.getticketinformation.mapper;

import com.ticket.getticketinformation.domain.rs.Arrival;
import com.ticket.getticketinformation.domain.rs.BaggageInfo;
import com.ticket.getticketinformation.domain.rs.BookingReference;
import com.ticket.getticketinformation.domain.rs.Cash;
import com.ticket.getticketinformation.domain.rs.Cheque;
import com.ticket.getticketinformation.domain.rs.CommissionInfo;
import com.ticket.getticketinformation.domain.rs.CouponHistory;
import com.ticket.getticketinformation.domain.rs.CouponInfo;
import com.ticket.getticketinformation.domain.rs.CouponValid;
import com.ticket.getticketinformation.domain.rs.Departure;
import com.ticket.getticketinformation.domain.rs.EMDInfo;
import com.ticket.getticketinformation.domain.rs.EquivFare;
import com.ticket.getticketinformation.domain.rs.FareAmount;
import com.ticket.getticketinformation.domain.rs.FareInfo;
import com.ticket.getticketinformation.domain.rs.FormOfPayment;
import com.ticket.getticketinformation.domain.rs.GetTicketInformationByTicketNumberResponse;
import com.ticket.getticketinformation.domain.rs.IdentityInfo;
import com.ticket.getticketinformation.domain.rs.IssuingAirlineInfo;
import com.ticket.getticketinformation.domain.rs.MarketingCarrierInfo;
import com.ticket.getticketinformation.domain.rs.Method;
import com.ticket.getticketinformation.domain.rs.OperatingCarrierInfo;
import com.ticket.getticketinformation.domain.rs.OriginalIssueInfo;
import com.ticket.getticketinformation.domain.rs.OriginDestination;
import com.ticket.getticketinformation.domain.rs.Other;
import com.ticket.getticketinformation.domain.rs.OtherPaymentMethodRemarks;
import com.ticket.getticketinformation.domain.rs.Payment;
import com.ticket.getticketinformation.domain.rs.PaymentCard;
import com.ticket.getticketinformation.domain.rs.SellAmount;
import com.ticket.getticketinformation.domain.rs.Status;
import com.ticket.getticketinformation.domain.rs.SubPayment;
import com.ticket.getticketinformation.domain.rs.Tax;
import com.ticket.getticketinformation.domain.rs.Taxes;
import com.ticket.getticketinformation.domain.rs.Terminal;
import com.ticket.getticketinformation.domain.rs.TicketDocumentInfo;
import com.ticket.getticketinformation.domain.rs.TicketInfo;
import com.ticket.getticketinformation.domain.rs.TotalAmount;
import com.ticket.getticketinformation.domain.rs.TotalTaxe;
import com.ticket.getticketinformation.domain.rs.Traveller;
import com.ticket.getticketinformation.domain.rs.UnstructuredFareCalcInfo;
import com.ticket.searchxml.response.ActualBaseFare;
import com.ticket.searchxml.response.AddlBaggageInfo;
import com.ticket.searchxml.response.ArrivalData;
import com.ticket.searchxml.response.ArrivalTerminalInfo;
import com.ticket.searchxml.response.BaseFare;
import com.ticket.searchxml.response.DepTerminalInfo;
import com.ticket.searchxml.response.DepartureData;
import com.ticket.searchxml.response.FormIDs;
import com.ticket.searchxml.response.MarketingCarrierData;
import com.ticket.searchxml.response.OperatingCarrier;
import com.ticket.searchxml.response.Payments;
import com.ticket.searchxml.response.SearchXmlTicketByTicketNumberRS;
import com.ticket.searchxml.response.TotalFare;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 按票号查询客票信息响应转换。
 * <p>SearchXmlTicketByTicketNumberRS 转为 GetTicketInformationByTicketNumberResponse。</p>
 */
@Mapper(componentModel = "spring")
public abstract class AbstractGetTicketInformationByTicketNumberConversionResponseDtoMapper {

    /**
     * 数字正则
     */
    public static final String NUM_FORMAT = "^[-\\+]?\\d+(\\.\\d*)?|\\.\\d+$";

    /**
     * 航班号正则
     */
    public static final String FLIGHT_NUMBER_REGEX = "[0-9]+[A-Za-z]$";

    /**
     * 空格
     */
    private static final String BLANK_SPACE = " ";

    /**
     * INSTANCE
     */
    public static final AbstractGetTicketInformationByTicketNumberConversionResponseDtoMapper INSTANCE =
            Mappers.getMapper(AbstractGetTicketInformationByTicketNumberConversionResponseDtoMapper.class);

    /**
     * 转换电子客票查询响应。response 对应报文 result。
     *
     * @param searchXmlTicketByTicketNumberRS 报文响应
     * @return 接口响应
     */
    @Mapping(target = "response.ticketInfo", source = "response.result.ticketInfo")
    public abstract GetTicketInformationByTicketNumberResponse conversion(
            SearchXmlTicketByTicketNumberRS searchXmlTicketByTicketNumberRS);

    /**
     * @param ticketInfo 报文客票信息
     * @return TicketInfo
     */
    @Mappings({
            @Mapping(target = "traveller", source = "traveller"),
            @Mapping(target = "ticketDocumentInfo", source = "ticketDocumentInfo"),
            @Mapping(target = "bookingReference", source = "bookingReference"),
            @Mapping(target = "issuingAirlineInfo", source = "issuingAirlineInfo"),
            @Mapping(target = "originalIssueInfo", source = "originalIssueInfo"),
            @Mapping(target = "originDestination", source = "originDestination"),
            @Mapping(target = "fareInfo", source = "fareInfo"),
            @Mapping(target = "commission", source = "commission"),
            @Mapping(target = "paymentList", source = "payments"),
            @Mapping(target = "endorsementInformation", source = "endorsementInformation")
    })
    public abstract TicketInfo conversionTicketInfo(com.ticket.searchxml.response.TicketInfo ticketInfo);

    /**
     * 婴儿标识由 Boolean 转为 String。
     *
     * @param traveller 报文旅客信息
     * @return Traveller
     */
    @Mappings({
            @Mapping(target = "infInd", source = "infantOnLapInd", qualifiedByName = "booleanToString"),
            @Mapping(target = "nameType", source = "nameType"),
            @Mapping(target = "lastName", source = "lastName"),
            @Mapping(target = "firstName", source = "firstName"),
            @Mapping(target = "fullName", source = "passengerName"),
            @Mapping(target = "passengerType", source = "passengerType")
    })
    public abstract Traveller conversionTraveller(com.ticket.searchxml.response.Traveller traveller);

    /**
     * @param bookingReference 报文订座引用
     * @return BookingReference
     */
    @Mappings({
            @Mapping(target = "internationalInd", source = "international"),
            @Mapping(target = "etType", source = "etType"),
            @Mapping(target = "governmentFareInd", source = "governmentFareInd")
    })
    public abstract BookingReference conversionBookingReference(
            com.ticket.searchxml.response.BookingReference bookingReference);

    /**
     * @param issuingAirlineInfo 报文出票航空公司
     * @return IssuingAirlineInfo
     */
    @Mappings({
            @Mapping(target = "airlineName", source = "airlineName"),
            @Mapping(target = "airline", source = "airline")
    })
    public abstract IssuingAirlineInfo conversionIssuingAirlineInfo(
            com.ticket.searchxml.response.IssuingAirlineInfo issuingAirlineInfo);

    /**
     * 出票日期 YYYY-MM-DD 转为 YYYYMMDD。城市代码取地理位置英文名称。
     *
     * @param originalIssueInfo 报文原票出票信息
     * @return OriginalIssueInfo
     */
    @Mappings({
            @Mapping(target = "agent", source = "agent"),
            @Mapping(target = "ticketingDate", source = "ticketingDate", qualifiedByName = "compactDate"),
            @Mapping(target = "cityCode", source = "locationEnName"),
            @Mapping(target = "ticketNumber", source = "ticketNumber")
    })
    public abstract OriginalIssueInfo conversionOriginalIssueInfo(
            com.ticket.searchxml.response.OriginalIssueInfo originalIssueInfo);

    /**
     * @param originDestination 报文始发到达
     * @return OriginDestination
     */
    @Mappings({
            @Mapping(target = "departureAirport", source = "departureCityCode"),
            @Mapping(target = "arrivalAirport", source = "arrivalCityCode")
    })
    public abstract OriginDestination conversionOriginDestination(
            com.ticket.searchxml.response.OriginDestination originDestination);

    /**
     * @param fareInfo 报文运价信息
     * @return FareInfo
     */
    @Mappings({
            @Mapping(target = "fareAmount", source = "baseFare"),
            @Mapping(target = "sellAmount", source = "actualBaseFare"),
            @Mapping(target = "equivFare", source = "equivFare"),
            @Mapping(target = "totalAmount", source = "totalFare"),
            @Mapping(target = "taxes", source = "taxes"),
            @Mapping(target = "unstructuredFareCalcInfo", source = "unstructuredFareCalcInfo")
    })
    public abstract FareInfo conversionFareInfo(com.ticket.searchxml.response.FareInfo fareInfo);

    /**
     * 票面价。金额保留两位小数。支付类型为 F 或 R 且票面价不为空时才返回货币代码。
     *
     * @param baseFare 报文基础运价
     * @return FareAmount
     */
    FareAmount conversionFareAmount(BaseFare baseFare) {
        if (ObjectUtils.isEmpty(baseFare)) {
            return null;
        }
        FareAmount fareAmount = new FareAmount();
        fareAmount.setFareBasis(formatFareBasis(baseFare.getFareBasis()));
        fareAmount.setPaymentTypeCode(baseFare.getPaymentTypeCode());
        if (isFareOrRefund(baseFare.getPaymentTypeCode()) && StringUtils.hasText(baseFare.getFareBasis())) {
            fareAmount.setCurrency(baseFare.getCurrency());
        }
        return fareAmount;
    }

    /**
     * 实际票面价。金额保留两位小数。
     *
     * @param actualBaseFare 报文实际基础运价
     * @return SellAmount
     */
    SellAmount conversionSellAmount(ActualBaseFare actualBaseFare) {
        if (ObjectUtils.isEmpty(actualBaseFare)) {
            return null;
        }
        SellAmount sellAmount = new SellAmount();
        sellAmount.setFareBasis(formatFareBasis(actualBaseFare.getFareBasis()));
        sellAmount.setCurrency(actualBaseFare.getCurrency());
        sellAmount.setPaymentTypeCode(actualBaseFare.getPaymentTypeCode());
        return sellAmount;
    }

    /**
     * 等值运价。金额保留两位小数。
     *
     * @param equivFare 报文等值运价
     * @return EquivFare
     */
    EquivFare conversionEquivFare(com.ticket.searchxml.response.EquivFare equivFare) {
        if (ObjectUtils.isEmpty(equivFare)) {
            return null;
        }
        EquivFare target = new EquivFare();
        target.setFareBasis(formatFareBasis(equivFare.getFareBasis()));
        target.setCurrency(equivFare.getCurrency());
        target.setPaymentTypeCode(equivFare.getPaymentTypeCode());
        return target;
    }

    /**
     * 票面总价。金额保留两位小数。
     *
     * @param totalFare 报文总运价
     * @return TotalAmount
     */
    TotalAmount conversionTotalAmount(TotalFare totalFare) {
        if (ObjectUtils.isEmpty(totalFare)) {
            return null;
        }
        TotalAmount totalAmount = new TotalAmount();
        totalAmount.setFareBasis(formatFareBasis(totalFare.getFareBasis()));
        totalAmount.setCurrency(totalFare.getCurrency());
        totalAmount.setAmountType(totalFare.getAmountType());
        totalAmount.setPaymentTypeCode(totalFare.getPaymentTypeCode());
        return totalAmount;
    }

    /**
     * 结果保留两位小数转换为 String。非数字原样返回。
     *
     * @param fareBasis 运价金额
     * @return 两位小数的金额文本
     */
    private static String formatFareBasis(String fareBasis) {
        if (!isEffectiveValue(fareBasis)) {
            return fareBasis;
        }
        return new BigDecimal(fareBasis).setScale(2, RoundingMode.HALF_UP).toString();
    }

    /**
     * 判断是否为数字。
     *
     * @param value 金额文本
     * @return 是否可按数字解析
     */
    private static boolean isEffectiveValue(String value) {
        if (StringUtils.hasText(value)) {
            Pattern pattern = Pattern.compile(NUM_FORMAT);
            return pattern.matcher(value).matches();
        }
        return false;
    }

    /**
     * 票面价类型是否为 F 或 R。
     *
     * @param paymentTypeCode 支付类型
     * @return 是否返回货币代码
     */
    private static boolean isFareOrRefund(String paymentTypeCode) {
        return "F".equalsIgnoreCase(paymentTypeCode) || "R".equalsIgnoreCase(paymentTypeCode);
    }

    /**
     * 税项列表对应报文 tax。总税费字符串前 3 位为货币码，其余为金额。
     *
     * @param taxes 报文税费
     * @return Taxes
     */
    @Mappings({
            @Mapping(target = "taxList", source = "tax"),
            @Mapping(target = "totalTaxAmount", source = "totalTaxAmount")
    })
    public abstract Taxes conversionTaxes(com.ticket.searchxml.response.Taxes taxes);

    /**
     * 拆分总税费字符串。
     *
     * @param totalTaxAmount 3 位货币代码加金额
     * @return TotalTaxe
     */
    TotalTaxe conversionTotalTaxe(String totalTaxAmount) {
        if (!StringUtils.hasText(totalTaxAmount)) {
            return null;
        }
        TotalTaxe totalTaxe = new TotalTaxe();
        if (totalTaxAmount.length() >= 3) {
            totalTaxe.setTaxCode(totalTaxAmount.substring(0, 3));
            totalTaxe.setValue(totalTaxAmount.substring(3));
        } else {
            totalTaxe.setValue(totalTaxAmount);
        }
        return totalTaxe;
    }

    /**
     * @param tax 报文税项
     * @return Tax
     */
    @Mappings({
            @Mapping(target = "amount", source = "amount"),
            @Mapping(target = "currency", source = "currency"),
            @Mapping(target = "taxCode", source = "taxCode"),
            @Mapping(target = "oldOrNewTaxInd", source = "taxTypes")
    })
    public abstract Tax conversionTax(com.ticket.searchxml.response.Tax tax);

    /**
     * 自动运价为 M 时，返回自动运价值、空格和运价文本的拼接；否则只返回文本。
     *
     * @param unstructuredFareCalcInfo 报文运价计算信息
     * @return UnstructuredFareCalcInfo
     */
    UnstructuredFareCalcInfo conversionUnstructuredFareCalcInfo(
            com.ticket.searchxml.response.UnstructuredFareCalcInfo unstructuredFareCalcInfo) {
        if (unstructuredFareCalcInfo == null) {
            return null;
        }
        UnstructuredFareCalcInfo target = new UnstructuredFareCalcInfo();
        if ("M".equals(unstructuredFareCalcInfo.getAutoFareType())) {
            target.setText(unstructuredFareCalcInfo.getAutoFareType() + BLANK_SPACE
                    + transformNullToString(unstructuredFareCalcInfo.getText()));
        } else {
            target.setText(unstructuredFareCalcInfo.getText());
        }
        return target;
    }

    /**
     * null 转为空串。
     *
     * @param value 文本
     * @return 非 null 文本
     */
    private static String transformNullToString(String value) {
        return value == null ? "" : value;
    }

    /**
     * @param commission 报文代理费
     * @return CommissionInfo
     */
    @Mappings({
            @Mapping(target = "commissionAmount", source = "commissionAmount"),
            @Mapping(target = "commissionType", source = "commissionType"),
            @Mapping(target = "currency", source = "currency")
    })
    public abstract CommissionInfo conversionCommissionInfo(com.ticket.searchxml.response.Commission commission);

    /**
     * 报文 payments 转为只含一个元素的付款列表。payment 列表转为 subPayment。
     *
     * @param payments 报文支付信息
     * @return 付款方式列表
     */
    List<Payment> conversionPaymentList(Payments payments) {
        if (ObjectUtils.isEmpty(payments)) {
            return null;
        }
        Payment payment = new Payment();
        payment.setFormOfPayment(conversionFormOfPayment(payments.getFormOfPayment()));
        List<SubPayment> subPaymentList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(payments.getPayment())) {
            for (com.ticket.searchxml.response.Payment paymentInfo : payments.getPayment()) {
                subPaymentList.add(conversionSubPayment(paymentInfo));
            }
        }
        payment.setSubPayment(subPaymentList);
        return Collections.singletonList(payment);
    }

    /**
     * @param formOfPayment 报文支付形式
     * @return FormOfPayment
     */
    @Mapping(target = "text", source = "text")
    public abstract FormOfPayment conversionFormOfPayment(com.ticket.searchxml.response.FormOfPayment formOfPayment);

    /**
     * @param payment 报文支付明细
     * @return SubPayment
     */
    @Mapping(target = "method", source = "method")
    public abstract SubPayment conversionSubPayment(com.ticket.searchxml.response.Payment payment);

    /**
     * @param method 报文支付方式
     * @return Method
     */
    @Mappings({
            @Mapping(target = "paymentCard", source = "paymentCard"),
            @Mapping(target = "cash", source = "cash"),
            @Mapping(target = "cheque", source = "cheque"),
            @Mapping(target = "other", source = "other")
    })
    public abstract Method conversionMethod(com.ticket.searchxml.response.Method method);

    /**
     * @param paymentCard 报文支付卡
     * @return PaymentCard
     */
    @Mappings({
            @Mapping(target = "cardCode", source = "cardCode"),
            @Mapping(target = "cardNo", source = "cardNo")
    })
    public abstract PaymentCard conversionPaymentCard(com.ticket.searchxml.response.PaymentCard paymentCard);

    /**
     * @param cash 报文现金支付
     * @return Cash
     */
    @Mapping(target = "text", source = "text")
    public abstract Cash conversionCash(com.ticket.searchxml.response.Cash cash);

    /**
     * @param cheque 报文支票支付
     * @return Cheque
     */
    @Mapping(target = "chequeFreeText", source = "chequeFreeText")
    public abstract Cheque conversionCheque(com.ticket.searchxml.response.Cheque cheque);

    /**
     * paymentTypeCode 对应到 otherPaymentMethodRemarks 子节点。
     *
     * @param other 报文其他支付方式
     * @return Other
     */
    Other conversionOther(com.ticket.searchxml.response.Other other) {
        if (other == null) {
            return null;
        }
        Other target = new Other();
        if (StringUtils.hasText(other.getPaymentTypeCode())) {
            OtherPaymentMethodRemarks remarks = new OtherPaymentMethodRemarks();
            remarks.setPaymentTypeCode(other.getPaymentTypeCode());
            target.setOtherPaymentMethodRemarks(remarks);
        }
        return target;
    }

    /**
     * 行程单打印标识为空时返回 false。出票日期、出票时间做格式压缩。
     *
     * @param ticketDocumentInfo 报文客票单据
     * @return TicketDocumentInfo
     */
    @Mappings({
            @Mapping(target = "receiptPrintedInd",
                    expression = "java(nullToFalse(ticketDocumentInfo.getAirportReceiptInd()))"),
            @Mapping(target = "insuranceInd", source = "insuranceInd"),
            @Mapping(target = "ticketNumber", source = "ticketNumber"),
            @Mapping(target = "conjunctionTotalTicket", source = "conjunctionTicketTotalNumber"),
            @Mapping(target = "conjunctionRelativeTicket", source = "conjunction"),
            @Mapping(target = "issueDate", source = "ticketingDate", qualifiedByName = "compactDate"),
            @Mapping(target = "issueTime", source = "issueTime", qualifiedByName = "compactTime"),
            @Mapping(target = "issueCityCode", source = "ticketingLocation"),
            @Mapping(target = "iataNumber", source = "iataNumber"),
            @Mapping(target = "remark", source = "remark"),
            @Mapping(target = "couponInfo", source = "couponInfo"),
            @Mapping(target = "tourCode", source = "tourCode"),
            @Mapping(target = "identityInfo", source = "formIDs"),
            @Mapping(target = "deviceTypeCode", source = "deviceTypeCode")
    })
    public abstract TicketDocumentInfo conversionTicketDocumentInfo(
            com.ticket.searchxml.response.TicketDocumentInfo ticketDocumentInfo);

    /**
     * 舱位优先取 classId，否则取市场方舱位。票联历史由单个节点包装为列表。
     *
     * @param couponInfo 报文票联
     * @return CouponInfo
     */
    @Mappings({
            @Mapping(target = "segmentId", source = "segmentId"),
            @Mapping(target = "couponNumber", source = "couponNumber"),
            @Mapping(target = "icsPnr", source = "icsPnr"),
            @Mapping(target = "crsPnr", source = "crsPnr"),
            @Mapping(target = "crsCode", source = "crsCode"),
            @Mapping(target = "fareBasis", source = "fareBasis"),
            @Mapping(target = "couponValid", source = "couponValid"),
            @Mapping(target = "status", source = "status"),
            @Mapping(target = "departure", source = "departure"),
            @Mapping(target = "arrival", source = "arrival"),
            @Mapping(target = "departureDayChange", source = "departureDayChange"),
            @Mapping(target = "arrivalDayChange", source = "arrivalDayChange"),
            @Mapping(target = "operatingCarrierInfo", source = "operatingCarrier"),
            @Mapping(target = "classId", expression = "java(getClassId(couponInfo))"),
            @Mapping(target = "marketingCarrierInfo", source = "marketingCarrierData"),
            @Mapping(target = "reservationStatusCode", source = "reservationStatusCode"),
            @Mapping(target = "baggageInfo", source = "addlBaggageInfo"),
            @Mapping(target = "stopOverInd", source = "isStopover"),
            @Mapping(target = "easyTripInd", source = "easyTripInd"),
            @Mapping(target = "emdInfo", source = "EMDInfo"),
            @Mapping(target = "mcoInfo", source = "mcoInfo"),
            @Mapping(target = "stpsType", source = "stpsType"),
            @Mapping(target = "noshowType", source = "noshowType"),
            @Mapping(target = "veidType", source = "veidType"),
            @Mapping(target = "invlType", source = "invlType"),
            @Mapping(target = "reissueCount", source = "revalidationTimes"),
            @Mapping(target = "couponHistory", source = "couponHistory", qualifiedByName = "toCouponHistoryList")
    })
    public abstract CouponInfo conversionCouponInfo(com.ticket.searchxml.response.CouponInfo couponInfo);

    /**
     * classId 有值则取该值，否则取 marketingCarrierData.mcRbd。
     *
     * @param couponInfo 报文票联
     * @return 舱位
     */
    @Named("getClassId")
    protected String getClassId(com.ticket.searchxml.response.CouponInfo couponInfo) {
        if (couponInfo == null) {
            return null;
        }
        if (StringUtils.hasText(couponInfo.getClassId())) {
            return couponInfo.getClassId();
        }
        if (couponInfo.getMarketingCarrierData() != null) {
            return couponInfo.getMarketingCarrierData().getMcRbd();
        }
        return null;
    }

    /**
     * 有效期日期 YYYY-MM-DD 转为 YYYYMMDD。expiryDate 对应 notValidAfter。
     *
     * @param couponValid 报文票联有效期
     * @return CouponValid
     */
    @Mappings({
            @Mapping(target = "notValidBefore", source = "notValidBefore", qualifiedByName = "compactDate"),
            @Mapping(target = "notValidAfter", source = "expiryDate", qualifiedByName = "compactDate")
    })
    public abstract CouponValid conversionCouponValid(com.ticket.searchxml.response.CouponValid couponValid);

    /**
     * @param status 报文航段状态
     * @return Status
     */
    @Mappings({
            @Mapping(target = "couponStatusCode", source = "couponStatus"),
            @Mapping(target = "airportControlAirline", source = "airportControlAirline")
    })
    public abstract Status conversionStatus(com.ticket.searchxml.response.Status status);

    /**
     * 航段离港信息。日期、时间压缩，航站楼取 depTerminalInfo。
     *
     * @param departure 报文离港信息
     * @return Departure
     */
    @Mappings({
            @Mapping(target = "departureAirport", source = "departureAirport"),
            @Mapping(target = "departureDate", source = "departureDate", qualifiedByName = "compactDate"),
            @Mapping(target = "departureTime", source = "departureTime", qualifiedByName = "compactTime"),
            @Mapping(target = "terminal", source = "depTerminalInfo")
    })
    public abstract Departure conversionDeparture(com.ticket.searchxml.response.Departure departure);

    /**
     * 票联历史离港信息。不填航站楼。
     *
     * @param departureData 报文离港数据
     * @return Departure
     */
    @Mappings({
            @Mapping(target = "departureAirport", source = "departureAirport"),
            @Mapping(target = "departureDate", source = "departureDate", qualifiedByName = "compactDate"),
            @Mapping(target = "departureTime", source = "departureTime", qualifiedByName = "compactTime"),
            @Mapping(target = "terminal", ignore = true)
    })
    public abstract Departure conversionDeparture(DepartureData departureData);

    /**
     * 登机号由 int 转为 String。
     *
     * @param depTerminalInfo 起飞航站楼
     * @return Terminal
     */
    @Mappings({
            @Mapping(target = "terminal", source = "departureTerminal"),
            @Mapping(target = "gate", source = "boardingNumber", qualifiedByName = "integerToString")
    })
    public abstract Terminal conversionTerminal(DepTerminalInfo depTerminalInfo);

    /**
     * 航段到港信息。
     *
     * @param arrival 报文到达信息
     * @return Arrival
     */
    @Mappings({
            @Mapping(target = "arrivalAirport", source = "arrivalAirport"),
            @Mapping(target = "arrivalDate", source = "arrivalDate", qualifiedByName = "compactDate"),
            @Mapping(target = "arrivalTime", source = "arrivalTime", qualifiedByName = "compactTime"),
            @Mapping(target = "terminal", source = "arrivalTerminalInfo")
    })
    public abstract Arrival conversionArrival(com.ticket.searchxml.response.Arrival arrival);

    /**
     * 票联历史到达信息。报文只有目的地代码 destination，不填日期、时间和航站楼。
     *
     * @param arrivalData 报文到达数据
     * @return Arrival
     */
    @Mappings({
            @Mapping(target = "arrivalAirport", source = "destination"),
            @Mapping(target = "arrivalDate", ignore = true),
            @Mapping(target = "arrivalTime", ignore = true),
            @Mapping(target = "terminal", ignore = true)
    })
    public abstract Arrival conversionArrival(ArrivalData arrivalData);

    /**
     * @param arrivalTerminalInfo 到达航站楼
     * @return Terminal
     */
    @Mappings({
            @Mapping(target = "terminal", source = "arrivalTerminal"),
            @Mapping(target = "gate", source = "arrivalGate")
    })
    public abstract Terminal conversionTerminal(ArrivalTerminalInfo arrivalTerminalInfo);

    /**
     * 航班号沿用报文字符串，不再做数值转换。
     *
     * @param operatingCarrier 报文承运方
     * @return OperatingCarrierInfo
     */
    @Mappings({
            @Mapping(target = "ocAirline", source = "ocAirline"),
            @Mapping(target = "ocFlightNumber", source = "flightNo"),
            @Mapping(target = "ocFlightSuffix", source = "ocFlightSuffix"),
            @Mapping(target = "ocClassId", source = "ocRbd")
    })
    public abstract OperatingCarrierInfo conversionOperatingCarrier(OperatingCarrier operatingCarrier);

    /**
     * 航段市场方信息。航班号取 flightNo。
     *
     * @param marketingCarrierData 报文市场方航班数据
     * @return MarketingCarrierInfo
     */
    @Mappings({
            @Mapping(target = "mcAirline", source = "mcAirline"),
            @Mapping(target = "mcFlightNumber", source = "flightNo"),
            @Mapping(target = "mcFlightSuffix", source = "mcFlightSuffix"),
            @Mapping(target = "mcClassId", source = "mcRbd")
    })
    public abstract MarketingCarrierInfo conversionMarketingCarrier(MarketingCarrierData marketingCarrierData);

    /**
     * @param addlBaggageInfo 报文附加行李
     * @return BaggageInfo
     */
    @Mapping(target = "freeBaggageAllowance", source = "freeBaggageAllowance")
    public abstract BaggageInfo conversionAddlBaggageInfo(AddlBaggageInfo addlBaggageInfo);

    /**
     * @param emdInfo 报文 EMD
     * @return EMDInfo
     */
    @Mappings({
            @Mapping(target = "ticketNumber", source = "ticketNumber"),
            @Mapping(target = "couponNumber", source = "couponNumber"),
            @Mapping(target = "ticketDocTypeCode", source = "ticketDocTypeCode"),
            @Mapping(target = "inConnectionInd", source = "isAccosicated")
    })
    public abstract EMDInfo conversionEMDInfo(com.ticket.searchxml.response.EMDInfo emdInfo);

    /**
     * 单个票联历史包装为列表。报文该节点基数为 0-1。
     *
     * @param couponHistory 报文票联历史
     * @return 票联历史列表
     */
    @Named("toCouponHistoryList")
    List<CouponHistory> toCouponHistoryList(com.ticket.searchxml.response.CouponHistory couponHistory) {
        if (couponHistory == null) {
            return null;
        }
        return Collections.singletonList(conversionCouponHistory(couponHistory));
    }

    /**
     * @param couponHistory 报文票联历史
     * @return CouponHistory
     */
    @Mappings({
            @Mapping(target = "sequenceNumber", source = "sequenceNumber"),
            @Mapping(target = "createDate", source = "createDate"),
            @Mapping(target = "createTime", source = "createTime"),
            @Mapping(target = "agent", source = "agent"),
            @Mapping(target = "departure", source = "departureData"),
            @Mapping(target = "arrival", source = "arrivalData"),
            @Mapping(target = "marketingCarrierInfo", source = "marketingCarrierInfo",
                    qualifiedByName = "conversionCouponHistoryMarketingCarrierInfo"),
            @Mapping(target = "reservationStatusCode", source = "reservationStatusCode"),
            @Mapping(target = "icsPnr", source = "icsPnr"),
            @Mapping(target = "crsPnr", source = "crsPnr")
    })
    public abstract CouponHistory conversionCouponHistory(com.ticket.searchxml.response.CouponHistory couponHistory);

    /**
     * @param formIDs 报文身份证明
     * @return IdentityInfo
     */
    @Mappings({
            @Mapping(target = "foidTypeCode", source = "foidTypeCode"),
            @Mapping(target = "foidNumber", source = "foidNumber")
    })
    public abstract IdentityInfo conversionIdentityInfo(FormIDs formIDs);

    /**
     * 票联历史市场方航班号为整数，先转为字符串再按末位字母拆分后缀。历史节点不填舱位。
     *
     * @param carrierInfo 报文市场方承运信息
     * @return MarketingCarrierInfo
     */
    @Named("conversionCouponHistoryMarketingCarrierInfo")
    MarketingCarrierInfo conversionCouponHistoryMarketingCarrierInfo(
            com.ticket.searchxml.response.MarketingCarrierInfo carrierInfo) {
        if (ObjectUtils.isEmpty(carrierInfo)) {
            return null;
        }
        MarketingCarrierInfo marketingCarrierInfo = new MarketingCarrierInfo();
        String flightNumber = carrierInfo.getMcFlightNumber() == null
                ? null : String.valueOf(carrierInfo.getMcFlightNumber());
        List<String> list = splitFlightNumberWithSuffix(flightNumber);
        marketingCarrierInfo.setMcAirline(carrierInfo.getMcAirline());
        marketingCarrierInfo.setMcFlightNumber(StringUtils.hasText(list.get(0)) ? list.get(0) : null);
        marketingCarrierInfo.setMcFlightSuffix(StringUtils.hasText(list.get(1)) ? list.get(1) : null);
        return marketingCarrierInfo;
    }

    /**
     * 拆分带后缀航班号。
     *
     * @param flightNumberWithSuffix 航班号，可能带一位字母后缀
     * @return 第一个为航班号，第二个为航班后缀
     */
    private static List<String> splitFlightNumberWithSuffix(String flightNumberWithSuffix) {
        String flightNumber = flightNumberWithSuffix;
        String fightSuffix = null;
        if (StringUtils.hasText(flightNumberWithSuffix)
                && Pattern.matches(FLIGHT_NUMBER_REGEX, flightNumberWithSuffix)) {
            flightNumber = flightNumberWithSuffix.substring(0, flightNumberWithSuffix.length() - 1);
            fightSuffix = flightNumberWithSuffix.substring(flightNumberWithSuffix.length() - 1);
        }
        return Arrays.asList(flightNumber, fightSuffix);
    }

    /**
     * YYYY-MM-DD 转为 YYYYMMDD。
     *
     * @param value 日期
     * @return 压缩后的日期
     */
    @Named("compactDate")
    String compactDate(String value) {
        if (!StringUtils.hasText(value)) {
            return value;
        }
        return value.replace("-", "");
    }

    /**
     * HH:mm 转为 HHmm。
     *
     * @param value 时间
     * @return 压缩后的时间
     */
    @Named("compactTime")
    String compactTime(String value) {
        if (!StringUtils.hasText(value)) {
            return value;
        }
        return value.replace(":", "");
    }

    /**
     * Boolean 转为 true 或 false 字符串。来源为空时保持为空。
     *
     * @param value 布尔值
     * @return 字符串
     */
    @Named("booleanToString")
    String booleanToString(Boolean value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    /**
     * 行程单打印标识为空时返回 false。
     *
     * @param value 能否在机场打印报销凭证
     * @return 打印标识
     */
    @Named("nullToFalse")
    Boolean nullToFalse(Boolean value) {
        return value == null ? Boolean.FALSE : value;
    }

    /**
     * Integer 转为 String。
     *
     * @param value 整数
     * @return 字符串
     */
    @Named("integerToString")
    String integerToString(Integer value) {
        if (value == null) {
            return null;
        }
        return String.valueOf(value);
    }
}
