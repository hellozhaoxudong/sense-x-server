package com.sense.app.core.agent.web;

import com.sense.app.core.agent.domain.CoreAiChat;
import com.sense.app.core.agent.service.CoreAiChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 智能聊天推荐
 */
@RestController
@RequestMapping("/api/cbi/smart/chat")
public class CoreAiChatController {


    @Autowired
    private CoreAiChatService chatService;


    /**
     * 新建/更新对话
     * @param chat 对话
     * @return 对话ID
     */
    @PostMapping("/submit")
    public ResponseEntity<Long> submit(@RequestParam(value = "agentType") String agentType,
                                       @RequestBody CoreAiChat chat) {
        Long chatId = chatService.submit(agentType, chat);
        return ResponseEntity.ok(chatId);
    }

    /**
     * 删除对话
     * @param chatId 对话ID
     * @return
     */
    @GetMapping("/delete")
    public ResponseEntity delete(@RequestParam(value = "chatId") Long chatId) {
        chatService.delete(chatId);
        return ResponseEntity.ok(true);
    }

    /**
     * 查询当前用户的所有对话
     * @return 对话列表
     */
    @GetMapping("/query")
    public ResponseEntity<List<CoreAiChat>> queryAll(@RequestParam("agentType") String agentType) {
        List<CoreAiChat> coreAiChats = chatService.queryAll(agentType);
        return ResponseEntity.ok(coreAiChats);
    }

}
