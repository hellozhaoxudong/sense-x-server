package com.sense.app.core.agent.web;

import com.sense.app.core.agent.domain.CoreAiChatMessage;
import com.sense.app.core.agent.service.CoreAiChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 智能对话日志
 */
@RestController
@RequestMapping("/api/cbi/smart/chat/message")
public class CoreAiChatMessageController {

    @Autowired
    private CoreAiChatMessageService chatMessageService;

    /**
     * 查询对话下的所有日志
     * @return 对话列表
     */
    @GetMapping("/query")
    public ResponseEntity<List<CoreAiChatMessage>> queryAll(@RequestParam(value = "chatId") Long chatId) {
        List<CoreAiChatMessage> smartChatMessages = chatMessageService.queryAll(chatId);
        return ResponseEntity.ok(smartChatMessages);
    }

    /**
     * 提交对话日志
     * @param chatMessage 对话日志
     * @return 对话日志ID
     */
    @PostMapping("/create")
    public ResponseEntity<Long> create(@RequestBody CoreAiChatMessage chatMessage) {
        Long chatMessageId = chatMessageService.create(chatMessage);
        return ResponseEntity.ok(chatMessageId);
    }

    /**
     * 批量删除对话日志
     * @return
     */
    @GetMapping("/delete")
    public ResponseEntity delete(@RequestParam("messageId") String messageId) {
        chatMessageService.delete(messageId);
        return ResponseEntity.ok(true);
    }
}
