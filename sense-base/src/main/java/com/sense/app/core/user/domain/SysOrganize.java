package com.sense.app.core.user.domain;

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
 * 组织部门信息
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("core_sys_organize")
public class SysOrganize extends BaseDomain {

    /**
     * 部门ID
     */
    @TableId
    private Long id;

    /**
     * 部门编码
     */
    @TableField
    private String organizeCode;

    /**
     * 部门名称
     */
    @TableField
    private String organizeName;

    /**
     * 父级菜单ID
     */
    @TableField
    private Long parentId;

    /**
     * 排序号
     */
    @TableField
    private Integer sortNum;


    /**
     * 子菜单
     */
    @TableField(exist = false)
    private List<SysOrganize> children;

}