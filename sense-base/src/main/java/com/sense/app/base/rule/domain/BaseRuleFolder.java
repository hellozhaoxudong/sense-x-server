package com.sense.app.base.rule.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sense.app.core.config.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 规则分类表
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("base_rule_folder")
public class BaseRuleFolder extends BaseDomain {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 规则分类名称
     */
    @TableField
    private String folderName;

    /**
     * 租户ID
     */
    @TableField
    private Long tenantId;

}
