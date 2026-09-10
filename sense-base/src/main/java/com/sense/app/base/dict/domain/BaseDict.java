package com.sense.app.base.dict.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sense.app.core.config.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 字典表
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("base_dict")
public class BaseDict extends BaseDomain {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 字典名称（如：用户状态）
     */
    @TableField
    private String dictName;

    /**
     * 字典编码（如：sys_user_status）
     */
    @TableField
    private String dictCode;

    @TableField
    private Long tenantId;

}
