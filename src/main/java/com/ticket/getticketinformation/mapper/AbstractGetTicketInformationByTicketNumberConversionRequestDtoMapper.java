package com.ticket.getticketinformation.mapper;

import com.ticket.getticketinformation.domain.rq.GetTicketInformationByTicketNumberRequest;
import com.ticket.getticketinformation.domain.rq.Query;
import com.ticket.searchxml.request.SearchXmlTicketByTicketNumberRQ;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * 按票号查询客票信息请求转换。
 * <p>GetTicketInformationByTicketNumberRequest 转为 SearchXmlTicketByTicketNumberRQ。</p>
 */
@Mapper(componentModel = "spring")
public abstract class AbstractGetTicketInformationByTicketNumberConversionRequestDtoMapper {

    /**
     * INSTANCE
     */
    public static final AbstractGetTicketInformationByTicketNumberConversionRequestDtoMapper INSTANCE =
            Mappers.getMapper(AbstractGetTicketInformationByTicketNumberConversionRequestDtoMapper.class);

    /**
     * 转换电子客票查询请求。
     * <p>airline、pointOfSale、locationInfo、customerID 在报文中无对应节点，不转换。
     * channelCode 固定封值为 PP。</p>
     *
     * @param getTicketInformationByTicketNumberRequest 接口请求信息
     * @return 电子客票查询请求
     */
    @Mapping(target = "query", source = "query")
    public abstract SearchXmlTicketByTicketNumberRQ conversion(
            GetTicketInformationByTicketNumberRequest getTicketInformationByTicketNumberRequest);

    /**
     * 转换查询条件。票号取 tkQueryInfo.ticketNumber，渠道代码固定为 PP。
     *
     * @param query 接口查询请求
     * @return 报文 Query
     */
    @Mapping(target = "xmlticketRequest.xmltKQuery.xmltKQueryInfo.ticketNumber", source = "tkQueryInfo.ticketNumber")
    @Mapping(target = "xmlticketRequest.xmltKQuery.xmltKQueryInfo.channelCode", constant = "PP")
    public abstract com.ticket.searchxml.request.Query conversionQuery(Query query);
}
