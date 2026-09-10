package com.sense.app.core.agent.web;


import com.sense.app.core.agent.dto.AgentMessage;
import com.sense.app.core.agent.manager.QaAgentManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/core/agent")
public class AgentWeb {

    @Autowired
    private QaAgentManager qaAgent;

    /**
     * 智能对话智能体
     * @param chatId    对话ID
     * @param modelId   模型ID
     * @param agentMessage  消息
     */
    @PostMapping("/qa")
    public SseEmitter qaAgent(@RequestParam(value = "chatId") String chatId,
                              @RequestParam(value = "modelId") Long modelId,
                              @RequestBody AgentMessage agentMessage) {
        return qaAgent.chat(chatId, modelId, agentMessage);
    }

}