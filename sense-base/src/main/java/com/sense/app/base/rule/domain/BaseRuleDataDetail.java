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
 * 规则数据详情
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("base_rule_data_detail")
public class BaseRuleDataDetail extends BaseDomain {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 所属规则ID
     */
    @TableField
    private Long ruleDataId;

    /**
     * 一行数据，JSON对象加密后的文本
     * SecurityUtils.importantDataEncryptRsa()加密
     */
    @TableField
    private String encryptData;

    /**
     * 租户ID
     */
    @TableField
    private Long tenantId;

    /**
     * 解密后的数据内容，仅用于查询展示，不持久化
     */
    @TableField(exist = false)
    private String dataContent;

}
