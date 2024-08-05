package com.dongye.lxs.chat.controller;

import com.dongye.lxs.chat.manager.SseEmitterStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
public class fastChatController {
    @GetMapping("/fastChat/sse/dashScope/{sessionId}")
    public SseEmitter getSseEmitter(@PathVariable String sessionId) {
        SseEmitter emitter = SseEmitterStore.getEmitter(sessionId);
        if (emitter == null) {
            emitter = new SseEmitter(10000L);
            SseEmitterStore.register(sessionId, emitter);
        }
        return emitter;
    }

}
