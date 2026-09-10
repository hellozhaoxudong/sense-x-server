package com.sense.app.core.agent.domain;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sense.app.core.config.BaseDomain;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 智能对话日志
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("core_ai_chat_message")
@Data
public class CoreAiChatMessage extends BaseDomain {

    /**
     * 消息ID
     */
    @TableId
    private Long id;

    /**
     * 消息UID
     */
    @TableField
    private String messageId;

    /**
     * 所属对话ID
     */
    @TableField
    private Long chatId;

    /**
     * 日志类型：Q(用户问题)
     */
    @TableField
    private String messageType;

    /**
     * 日志内容（存储的是json字符串）
     */
    @TableField
    private String content;

    /**
     * 思考内容
     */
    @TableField
    private String thinks;

    /**
     * 所属用户ID
     */
    @TableField
    private Long adminId;

    /**
     * 租户ID
     */
    @TableField
    private Long tenantId;

    public static List<ChatMessage> parseMessage(List<CoreAiChatMessage> messages){
        if (CollUtil.isEmpty(messages)){
            return new ArrayList<>();
        }

        return messages.stream().map(message -> {
            if ("USER".equals(message.getMessageType())){
                return UserMessage.from(message.getContent());
            } else if ("AI".equals(message.getMessageType())){
                return AiMessage.from(message.getContent());
            }  else if ("SYSTEM".equals(message.getMessageType())){
                return SystemMessage.from(message.getContent());
            }

            return AiMessage.from(message.getContent());
        }).toList();
    }
}
