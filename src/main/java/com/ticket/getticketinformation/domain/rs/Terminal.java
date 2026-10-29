package com.ticket.getticketinformation.domain.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * 航站楼信息。
 */
@Getter
@Setter
public class Terminal {

    /**
     * 航站楼。
     */
    private String terminal;

    /**
     * 登机口或到达口。登机号由 int 转为 String。
     */
    private String gate;

}
