package com.sense.app.core.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sense.app.core.config.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 系统菜单
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("core_sys_menu")
public class CoreSysMenu extends BaseDomain {

    /**
     * 菜单ID
     */
    @TableId
    private Long id;

    /**
     * 应用ID
     */
    @TableField
    private Long appId;

    /**
     * 菜单编码
     */
    @TableField
    private String menuCode;

    /**
     * 菜单图标
     */
    @TableField
    private String menuIcon;

    /**
     * 菜单名称
     */
    @TableField
    private String menuName;

    /**
     * 父菜单ID
     */
    @TableField
    private Long parentId;

    /**
     * 菜单路由
     */
    @TableField
    private String menuPath;

    /**
     * 排序号
     */
    @TableField
    private Integer sortOrder;

    /**
     * 子菜单列表（非数据库字段，用于树形结构）
     */
    @TableField(exist = false)
    private List<CoreSysMenu> children;

    /**
     * 应用名称
     */
    @TableField(exist = false)
    private String appName;

    /**
     * 菜单别名
     * 当菜单分配给租户或角色时，可以重新定义菜单别名，用于展示
     */
    @TableField(exist = false)
    private String menuAliasName;

}
