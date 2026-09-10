package com.sense.app.core.agent.manager;


import com.sense.app.core.agent.domain.CoreAiModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ModelManager {

    /**
     * 构建流式对话模型
     */
    public OpenAiStreamingChatModel buildStreamModel(CoreAiModel setting) {
        return OpenAiStreamingChatModel.builder()
                .baseUrl(setting.getBaseUrl())
                .modelName(setting.getModelName())
                .apiKey(setting.getApiKey())
                .sendThinking(true)
                .build();
    }
}
