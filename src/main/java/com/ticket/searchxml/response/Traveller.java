package com.ticket.searchxml.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 旅客信息。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 traveller。</p>
 */
@Getter
@Setter
public class Traveller {

    /**
     * 常旅客信息列表。
     * <p>类型：fQTV；约束：O（可选）；基数：0-N（List）。subfQTV 的属性已上收到 FQTV。</p>
     */
    private List<FQTV> fQTV;

    /**
     * 名。
     * <p>类型：string；约束：O（可选）；基数：0-1；示例：[A-Za-z]*。</p>
     */
    private String firstName;

    /**
     * 婴儿不占座标识。
     * <p>类型：boolean；约束：O（可选）；基数：0-1。</p>
     */
    private Boolean infantOnLapInd;

    /**
     * 姓。
     * <p>类型：string；约束：M（必填）；基数：1；示例：[A-Za-z]*。</p>
     */
    private String lastName;

    /**
     * 姓名中英文标识。
     * <p>类型：int；约束：O（可选）；基数：0-1。</p>
     */
    private Integer nameType;

    /**
     * 全名。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String passengerName;

    /**
     * 旅客类型。
     * <p>类型：string；约束：O（可选）；基数：0-1。</p>
     */
    private String passengerType;

    /**
     * 旅客编号。
     * <p>类型：int；约束：O（可选）；基数：0-1；示例：1。</p>
     */
    private Integer paxId;
}
