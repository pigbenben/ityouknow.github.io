package com.ticket.getticketinformation.domain.rq;

import lombok.Getter;
import lombok.Setter;

/**
 * 查询请求。
 */
@Getter
@Setter
public class Query {

    /**
     * 票价查询信息。
     */
    private TkQueryInfo tkQueryInfo;

    /**
     * 销售点信息。报文无对应节点，不参与转换。
     */
    private PointOfSale pointOfSale;

}
