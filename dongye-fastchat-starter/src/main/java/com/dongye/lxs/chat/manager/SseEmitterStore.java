package com.dongye.lxs.chat.manager;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * SseEmitter 存储类
 * @Date 2024/8/5 16:00
 * @Author 李祥生
 */
public class SseEmitterStore {

    private static final ConcurrentMap<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    // 注册 SseEmitter
    public static void register(String sessionId, SseEmitter emitter) {
        // 设置回调，确保在完成、超时或错误时移除 SseEmitter
        emitter.onCompletion(() -> emitterMap.remove(sessionId));
        emitter.onTimeout(() -> emitterMap.remove(sessionId));
        emitter.onError(e -> emitterMap.remove(sessionId));

        emitterMap.put(sessionId, emitter);
    }

    // 获取 SseEmitter
    public static SseEmitter getEmitter(String sessionId) {
        return emitterMap.get(sessionId);
    }

    // 移除 SseEmitter
    public static void remove(String sessionId) {
        emitterMap.remove(sessionId);
    }

    // 获取所有 sessionIds
    public static ConcurrentMap<String, SseEmitter> getAllEmitters() {
        return emitterMap;
    }
}
