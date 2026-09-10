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
 * 规则表
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("base_rule")
public class BaseRule extends BaseDomain {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 所属分类ID
     */
    @TableField
    private Long folderId;

    /**
     * 规则编码
     */
    @TableField
    private String ruleCode;

    /**
     * 规则名称
     */
    @TableField
    private String ruleName;

    /**
     * 规则状态：编辑中、已上线
     */
    @TableField
    private String ruleStatus;

    /**
     * 规则内容：线上版
     */
    @TableField
    private String onlineContent;

    /**
     * 规则内容：编辑版
     */
    @TableField
    private String editContent;

    /**
     * 租户ID
     */
    @TableField
    private Long tenantId;

}
