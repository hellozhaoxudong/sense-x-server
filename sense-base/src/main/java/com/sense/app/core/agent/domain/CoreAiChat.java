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
 * 智能对话记录
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("core_ai_chat")
@Data
public class CoreAiChat extends BaseDomain {

    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 智能体类型
     */
    @TableField
    private String agentType;

    /**
     * 对话标题
     */
    @TableField
    private String chatTitle;

    /**
     * 用户ID
     */
    @TableField
    private Long adminId;

    /**
     * 租户ID
     */
    @TableField
    private Long tenantId;
}
