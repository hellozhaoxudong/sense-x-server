package com.sense.app.core.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sense.app.core.config.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.fesod.sheet.annotation.ExcelProperty;

import java.io.Serializable;

/**
 * @ClassName SysRole
 * @description 角色信息
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("core_sys_role")
public class SysRole extends BaseDomain implements Serializable {

    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 编码
     */
    @TableField
    @ExcelProperty("角色编码")
    private String roleCode;

    /**
     * 名称
     */
    @TableField
    @ExcelProperty("角色名称")
    private String roleName;

    /**
     * 租户ID
     */
    @TableField
    private Long tenantId;

}