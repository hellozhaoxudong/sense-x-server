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
 * @ClassName SysUserOrganize
 * @description 用户部门分配信息
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("core_sys_user_organize")
public class SysUserOrganize extends BaseDomain {

    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 部门ID
     */
    @TableField
    private Long organizeId;

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
     * 部门编码
     */
    @TableField(exist = false)
    private String organizeCode;

    /**
     * 部门名称
     */
    @TableField(exist = false)
    private String organizeName;

}