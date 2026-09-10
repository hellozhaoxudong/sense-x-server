package com.sense.app.core.thread;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;


@Component
@Order(-1)
public class ChatThreadPool implements ApplicationRunner {

    public static ThreadPoolTaskExecutor ChatThread = null;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ChatThread = new ThreadPoolTaskExecutor();
        ChatThread.setCorePoolSize(1000);
        ChatThread.setMaxPoolSize(20000);
        ChatThread.setQueueCapacity(20000);
        ChatThread.setKeepAliveSeconds(200);
        ChatThread.setThreadNamePrefix("AI智能体对话线程池");
        ChatThread.setThreadGroupName("SENSE-AGENT-");
        ChatThread.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        ChatThread.initialize();
    }
}
