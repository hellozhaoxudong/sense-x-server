package com.sense.app.core.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sense.app.core.config.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统菜单-角色分配
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("core_sys_menu_role")
public class CoreSysMenuRole extends BaseDomain {

    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 菜单ID
     */
    @TableField
    private Long menuId;

    /**
     * 菜单别名
     * 初始来源：租户菜单的别名
     */
    @TableField
    private String menuAliasName;

    /**
     * 租户ID
     */
    @TableField
    private Long tenantId;

    /**
     * 角色ID
     */
    @TableField
    private Long roleId;

}
