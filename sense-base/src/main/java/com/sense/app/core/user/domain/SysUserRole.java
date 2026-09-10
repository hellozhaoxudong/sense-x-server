package com.sense.app.core.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sense.app.core.config.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName SysUserRole
 * @description 用户角色分配信息
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("core_sys_user_role")
public class SysUserRole extends BaseDomain implements Serializable {

    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 角色ID
     */
    @TableField
    private Long roleId;

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
     * 角色编码
     */
    @TableField(exist = false)
    private String roleCode;

    /**
     * 角色名称
     */
    @TableField(exist = false)
    private String roleName;
}