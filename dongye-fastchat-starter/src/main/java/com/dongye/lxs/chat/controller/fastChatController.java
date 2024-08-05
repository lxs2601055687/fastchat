package com.dongye.lxs.chat.controller;

import com.dongye.lxs.chat.manager.SseEmitterStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
public class fastChatController {
    /**
     * 获取SseEmitter
     * @param sessionId
     * @return
     * 现在该SDK仅支持阿里云的DASHSCOPE模型
     * 后续优化可以将模型作为参数传递解决接口的通用性
     */
    @GetMapping("/fastChat/sse/dashScope/{sessionId}")
    public SseEmitter getSseEmitter(@PathVariable String sessionId) {
        SseEmitter emitter = SseEmitterStore.getEmitter(sessionId);
        if(emitter == null) {
            throw new RuntimeException("SseEmitter not found");
        }
        return emitter;
    }

}
