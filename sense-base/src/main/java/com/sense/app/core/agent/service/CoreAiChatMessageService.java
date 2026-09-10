package com.sense.app.core.agent.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.core.agent.domain.CoreAiChatMessage;
import com.sense.app.core.agent.mapper.CoreAiChatMessageMapper;
import com.sense.app.core.exception.BizException;
import com.sense.app.core.utils.CurrentUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 智能对话日志
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CoreAiChatMessageService extends ServiceImpl<CoreAiChatMessageMapper, CoreAiChatMessage> {

    /**
     * 创建对话日志
     * @param chatMessage 对话日志
     * @return 对话日志ID
     */
    public Long create(CoreAiChatMessage chatMessage) {

        if (null == chatMessage.getChatId()) {
            throw new BizException("对话ID不能为空");
        }

        if (StrUtil.isBlank(chatMessage.getMessageType())) {
            throw new BizException("对话类型不能为空");
        }

        if (StrUtil.isBlank(chatMessage.getMessageId())) {
            throw new BizException("消息ID不能为空");
        }

        if (StrUtil.isBlank(chatMessage.getContent())) {
            throw new BizException("对话内容不能为空");
        }

        Long chatMessageId = IdWorker.getId();

        chatMessage.setId(chatMessageId);
        chatMessage.setAdminId(CurrentUser.getUserId());
        baseMapper.insert(chatMessage);

        return chatMessageId;
    }

    /**
     * 批量删除对话日志
     * @param ids 对话日志ID列表
     * @return
     */
    public void delete(String messageId) {
        baseMapper.delete(new LambdaQueryWrapper<CoreAiChatMessage>().eq(CoreAiChatMessage::getMessageId, messageId));
    }

    /**
     * 查询对话下的所有日志
     * @return 对话列表
     */
    public List<CoreAiChatMessage> queryAll(Long chatId) {
        return baseMapper.selectList(new LambdaQueryWrapper<CoreAiChatMessage>().eq(CoreAiChatMessage::getChatId, chatId));
    }
}
