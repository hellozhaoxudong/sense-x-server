package com.sense.app.core.agent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgentMessage {

    // 角色
    private String role;

    // 消息内容
    private String content;

}
