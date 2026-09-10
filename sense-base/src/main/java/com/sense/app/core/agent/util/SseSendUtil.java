package com.sense.app.core.agent.util;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

public class SseSendUtil {

    /**
     * 发送思考
     */
    public static void sendThink(SseEmitter emitter, String think) {
        SseEmitter.SseEventBuilder sseEventBuilder = SseEmitter.event().name("think").data(think);
        try {
            emitter.send(sseEventBuilder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送数据
     */
    public static void sendData(SseEmitter emitter, String data) {
        SseEmitter.SseEventBuilder sseEventBuilder = SseEmitter.event().name("data").data(data);
        try {
            emitter.send(sseEventBuilder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送数据
     */
    public static void sendError(SseEmitter emitter, String data) {
        SseEmitter.SseEventBuilder sseEventBuilder = SseEmitter.event().name("error").data(data);

        try {
            emitter.send(sseEventBuilder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送完成
     */
    public static void sendCompleted(SseEmitter emitter, String data) {
        SseEmitter.SseEventBuilder sseEventBuilder = SseEmitter.event().name("completed").data(data);
        try {
            emitter.send(sseEventBuilder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
