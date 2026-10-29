package com.ticket.getticketinformation.domain.rq;

import lombok.Getter;
import lombok.Setter;

/**
 * 销售点信息。
 */
@Getter
@Setter
public class PointOfSale {

    /**
     * 销售渠道。转换时不取该值，channelCode 固定为 PP。
     */
    private String channel;

    /**
     * 位置信息。报文无对应节点，不参与转换。
     */
    private LocationInfo locationInfo;

}
