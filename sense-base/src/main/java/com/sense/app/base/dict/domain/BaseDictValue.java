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
 * 字典值表
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("base_dict_value")
public class BaseDictValue extends BaseDomain {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 字典编码
     */
    @TableField
    private String dictCode;

    /**
     * 字典值
     */
    @TableField
    private String valueCode;

    /**
     * 字典值名
     */
    @TableField
    private String valueName;

    /**
     * 排序
     */
    @TableField
    private Integer sort;

    /**
     * 状态Y/N
     */
    @TableField
    private String status;

    @TableField
    private Long tenantId;
}
