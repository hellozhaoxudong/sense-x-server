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
 * 规则数据
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("base_rule_data")
public class BaseRuleData extends BaseDomain {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 规则数据编码，脚本中使用本编码可获取到规则数据
     */
    @TableField
    private String dataCode;

    /**
     * 规则数据名称
     */
    @TableField
    private String dataName;

    /**
     * 租户ID
     */
    @TableField
    private Long tenantId;

}
