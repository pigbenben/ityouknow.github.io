package com.ticket.searchxml.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 身份证明。
 * <p>所属接口：SearchXmlTicketByTicketNumber 响应报文，节点 formIDs。原 sub 节点已去掉，其属性上收到本类。</p>
 */
@Getter
@Setter
public class FormIDs {

    /**
     * 票联号。
     * <p>类型：int；约束：O（可选）；基数：0-1；示例：\d 数字。</p>
     */
    private Integer couponNumber;

    /**
     * 身份证明号码。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String foidNumber;

    /**
     * 身份证明类型。
     * <p>类型：string；约束：M（必填）；基数：1。</p>
     */
    private String foidTypeCode;
}
