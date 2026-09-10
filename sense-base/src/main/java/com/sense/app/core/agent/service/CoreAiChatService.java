package com.sense.app.core.agent.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.core.agent.domain.CoreAiChat;
import com.sense.app.core.agent.domain.CoreAiChatMessage;
import com.sense.app.core.agent.mapper.CoreAiChatMapper;
import com.sense.app.core.agent.mapper.CoreAiChatMessageMapper;
import com.sense.app.core.utils.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 智能对话记录
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CoreAiChatService extends ServiceImpl<CoreAiChatMapper, CoreAiChat> {

    @Autowired
    private CoreAiChatMessageMapper chatMessageMapper;

    /**
     * 提交对话
     * @param chat 对话
     * @return 对话ID
     */
    public Long submit(String agentType, CoreAiChat chat) {
        if (null == chat.getId()) {
            // 新建对话
            chat.setId(IdWorker.getId());
            chat.setAgentType(agentType);
            chat.setAdminId(CurrentUser.getUserId());
            chat.setChatTitle("新对话");
            save(chat);
        } else {
            updateById(chat);
        }

        return chat.getId();
    }

    /**
     * 删除对话记录
     * @param chatId 对话记录ID
     */
    public void delete(Long chatId) {
        // 删除对话日志
        chatMessageMapper.delete(new LambdaQueryWrapper<CoreAiChatMessage>().eq(CoreAiChatMessage::getChatId, chatId));

        // 删除对话记录
        baseMapper.deleteById(chatId);
    }

    /**
     * 查询当前用户的所有对话
     * @return 对话列表
     */
    public List<CoreAiChat> queryAll(String agentType) {
        Long userId = CurrentUser.getUserId();
        return baseMapper.selectList(new LambdaQueryWrapper<CoreAiChat>().eq(CoreAiChat::getAdminId, userId).eq(CoreAiChat::getAgentType, agentType));
    }
}
