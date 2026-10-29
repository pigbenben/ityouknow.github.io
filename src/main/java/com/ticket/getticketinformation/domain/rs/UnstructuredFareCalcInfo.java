package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 运价计算信息。
 */
@Getter
@Setter
public class UnstructuredFareCalcInfo {

    /**
     * 运价计算文本。自动运价为 M 时拼接运价代码。
     */
    private String text;

}
