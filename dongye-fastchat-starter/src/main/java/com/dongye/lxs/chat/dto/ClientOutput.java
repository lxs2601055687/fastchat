package com.dongye.lxs.chat.dto;

import com.dongye.lxs.chat.bean.ChatContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Data
@Builder
@AllArgsConstructor
public class ClientOutput {


    private ChatContext context;


    private String answer;


    private String sessionId;

    private String sseUrl;


}
