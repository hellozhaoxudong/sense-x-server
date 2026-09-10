package com.sense.app.core.user.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomTenant implements Serializable {

    /**
     * 租户ID
     */
    private Long id;

    /**
     * 租户编码
     * 租户编码
     */
    private String tenantCode;

    /**
     * 租户名称
     */
    private String tenantName;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 启用状态, 1:启用, 0:禁用
     */
    private Integer enabled;

}