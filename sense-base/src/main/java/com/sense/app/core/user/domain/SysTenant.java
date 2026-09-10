package com.sense.app.core.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sense.app.core.config.BaseDomain;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName SysTenant
 * @description 租户信息
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Data
@TableName("core_sys_tenant")
public class SysTenant extends BaseDomain implements Serializable {

    /**
     * 租户ID
     */
    @TableId
    private Long id;

    /**
     * 租户编码
     */
    @TableField
    private String tenantCode;

    /**
     * 租户名称
     */
    @TableField
    private String tenantName;

    /**
     * 公司名称
     */
    @TableField
    private String companyName;

    /**
     * 用户数限制
     */
    @TableField
    private Long userLimit;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField
    private Date expirationDate;

    /**
     * 启用状态, 1:启用, 0:禁用
     */
    @TableField
    private Integer enabled;

    /**
     * 租户管理员用户信息
     */
    @TableField(exist = false)
    private SysUser adminUser;

}