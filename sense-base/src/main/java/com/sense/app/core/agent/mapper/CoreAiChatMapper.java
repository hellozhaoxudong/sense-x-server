package com.sense.app.core.agent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sense.app.core.agent.domain.CoreAiChat;
import org.apache.ibatis.annotations.Mapper;

/**
 * 智能对话记录
 */
@Mapper
public interface CoreAiChatMapper extends BaseMapper<CoreAiChat> {
}
