package com.sense.app.base.rule.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sense.app.core.config.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 规则运行日志表
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("base_rule_run")
public class BaseRuleRun extends BaseDomain {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 规则ID
     */
    @TableField
    private String ruleId;

    /**
     * 规则内容
     */
    @TableField
    private String ruleContent;

    /**
     * 传入参数
     */
    @TableField
    private String ruleParams;

    /**
     * 触发时间
     */
    @TableField
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startDate;

    /**
     * 结束时间
     */
    @TableField
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;

    /**
     * 运行结果
     */
    @TableField
    private String runResult;

    /**
     * 租户ID
     */
    @TableField
    private Long tenantId;

    // === 非持久化字段，用于列表展示 ===

    /**
     * 规则分类名称
     */
    @TableField(exist = false)
    private String folderName;

    /**
     * 规则编码
     */
    @TableField(exist = false)
    private String ruleCode;

    /**
     * 规则名称
     */
    @TableField(exist = false)
    private String ruleName;

}
