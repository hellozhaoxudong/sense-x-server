package com.sense.app.core.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sense.app.core.config.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName SysUserTenant
 * @description 用户租户分配信息
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("core_sys_user_tenant")
public class SysUserTenant extends BaseDomain {

    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 用户ID
     */
    @TableField
    private Long userId;

    /**
     * 租户ID
     */
    @TableField
    private Long tenantId;

    /**
     * 是否为主租户：1（主租户）/ 0（非租户）
     */
    @TableField
    private Integer mainTenant;

    /**
     * 租户编码
     */
    @TableField(exist = false)
    private String tenantCode;

    /**
     * 租户名称
     */
    @TableField(exist = false)
    private String tenantName;
}