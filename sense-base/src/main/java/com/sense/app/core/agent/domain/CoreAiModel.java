package com.sense.app.core.agent.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sense.app.core.config.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 模型
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("core_ai_model")
@Data
public class CoreAiModel extends BaseDomain {

    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 模型类型：Chat、Embedding
     */
    @TableField
    private String modelType;

    /**
     * 模型标题
     */
    @TableField
    private String modelTitle;

    /**
     * 模型名称
     */
    @TableField
    private String modelName;

    /**
     * 调用地址
     */
    @TableField
    private String baseUrl;

    /**
     * KEY
     */
    @TableField
    private String apiKey;

    /**
     * 是否启用：Y/N
     */
    @TableField
    private String enable;

    /**
     * 租户ID
     */
    @TableField
    private Long tenantId;
}
