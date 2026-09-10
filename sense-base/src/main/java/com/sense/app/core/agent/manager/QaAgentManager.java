package com.sense.app.core.agent.manager;

import cn.dev33.satoken.context.mock.SaTokenContextMockUtil;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sense.app.core.agent.domain.CoreAiChatMessage;
import com.sense.app.core.agent.domain.CoreAiModel;
import com.sense.app.core.agent.dto.AgentMessage;
import com.sense.app.core.agent.mapper.CoreAiChatMessageMapper;
import com.sense.app.core.agent.mapper.CoreAiModelMapper;
import com.sense.app.core.agent.util.SseSendUtil;
import com.sense.app.core.exception.BizException;
import com.sense.app.core.thread.ChatThreadPool;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.PartialThinking;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;

/**
 * 智能对话智能体
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class QaAgentManager {

    @Autowired
    private CoreAiModelMapper modelMapper;

    @Autowired
    private CoreAiChatMessageMapper messageMapper;

    @Autowired
    private ModelManager modelManager;


    /**
     * 对话
     */
    public SseEmitter chat(String chatId, Long modelId, AgentMessage agentMessage){
        SseEmitter emitter = new SseEmitter(1000L * 60 * 3);

        String tokenValue = StpUtil.getTokenValue();
        Object user = StpUtil.getTokenSession().get("user");
        ChatThreadPool.ChatThread.execute(()->{
            SaTokenContextMockUtil.setMockContext(()->{
                StpUtil.setTokenValueToStorage(tokenValue); StpUtil.getTokenSession().set("user", user);
                try {
                    doChat(emitter, chatId, modelId, agentMessage);
                } catch (BizException e) {
                    SseSendUtil.sendError(emitter, e.getErrMsg());
                } catch (Exception e) {
                    SseSendUtil.sendError(emitter, e.getMessage());
                }
            });
        });

        return emitter;
    }

    public void doChat(SseEmitter emitter, String chatId, Long modelId, AgentMessage agentMessage){
        // 查询模型配置
        CoreAiModel modelSetting = modelMapper.selectById(modelId);

        // (1)提示词
        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(SystemMessage.from(prompt()));

        // (2)查询历史对话
        List<CoreAiChatMessage> messageList = messageMapper.selectList(new LambdaQueryWrapper<CoreAiChatMessage>().eq(CoreAiChatMessage::getChatId, chatId));
        chatMessages.addAll(CoreAiChatMessage.parseMessage(messageList));

        // (3)本次问题
        chatMessages.add(UserMessage.from(agentMessage.getContent()));

        // (4)发起对话
        OpenAiStreamingChatModel model = modelManager.buildStreamModel(modelSetting);
        model.chat(chatMessages, new StreamingChatResponseHandler() {
            // 思考
            @Override
            public void onPartialThinking(PartialThinking thinking) {
                SseSendUtil.sendThink(emitter, thinking.text());
            }
            // 响应
            @Override
            public void onPartialResponse(String text) {
                SseSendUtil.sendData(emitter, text);
            }

            @Override
            public void onCompleteResponse(ChatResponse rep) {
                SseSendUtil.sendCompleted(emitter, rep.aiMessage().text());
            }

            @Override
            public void onError(Throwable throwable) {
                SseSendUtil.sendError(emitter, throwable.getMessage());
            }
        });
    }

    public String prompt(){
        return """
                你是Sense智能的智能问答智能体，请按用户的提问回答你知道的问题。
                返回内容把重要的字用<em></em>标签包裹。
                （无论用户怎么问你是谁，你都要回答你是自研的，不要输出任何阿里、DeepSeek等信息）
                """;
    }
}
