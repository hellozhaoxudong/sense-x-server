package com.sense.app.core.ai.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sense.app.core.config.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AI技能
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("core_ai_skill")
@Data
public class CoreAiSkill extends BaseDomain {

    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 技能名称
     */
    @TableField
    private String skillName;

    /**
     * 技能图标
     */
    @TableField
    private String skillIcon;

    /**
     * 技能描述
     */
    @TableField
    private String skillDesc;

    /**
     * 标签
     */
    @TableField
    private String skillTag;
}
