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
 * 系统应用
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("core_sys_app")
public class CoreSysApp extends BaseDomain {

    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 应用编码
     */
    @TableField
    private String appCode;

    /**
     * 应用名称
     */
    @TableField
    private String appName;

    /**
     * 应用图标
     */
    @TableField
    private String appIcon;

    /**
     * 排序号
     */
    @TableField
    private Integer sortOrder;

    /**
     * 本应用下的菜单树
     */
    @TableField(exist = false)
    private List<CoreSysMenu> menuList;

}
