package com.dongye.lxs.demo.service;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.dongye.lxs.chat.bean.ChatContext;
import com.dongye.lxs.chat.bean.Message;
import com.dongye.lxs.chat.client.DyChatClient;
import com.dongye.lxs.chat.constant.ModelSource;
import com.dongye.lxs.chat.dto.ClientInput;
import com.dongye.lxs.chat.dto.ClientOutput;
import com.dongye.lxs.chat.dto.ClientResponse;
import com.dongye.lxs.chat.exception.ClientInputValidationException;
import com.dongye.lxs.demo.Dto.fastChatRequestDto;
import com.fasterxml.jackson.databind.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class fastChatDemoService {
    /**
     * 普通询问，非流式传输
     * @param fastChatRequestDto
     * @return
     */
    public ClientResponse<ClientOutput> normalAsk(fastChatRequestDto fastChatRequestDto) {

        //modelSourceSSE流式传输或普通传输
        //requestId请求id，用于区分每次请求 实现幂等
        ClientInput clientInput = ClientInput.builder()
                .modelSource(ModelSource.DASHSCOPE_Normal)
                .requestId(UUID.randomUUID().toString().replace("-", ""))
                .build();
        BeanUtils.copyProperties(fastChatRequestDto, clientInput);

        ClientOutput data = null;
        try {
            ClientResponse<Object> call = DyChatClient.call(clientInput);
            data = (ClientOutput) call.getData();
            ChatContext context = data.getContext();
            if (context != null) {
                log.info("context:{}", context);
            }
            //summary问答总结
            String summary = context.getSummary();
            //历史记录，这里应该将记录更新到对应的聊天记录表中
            List<Message> messageList = context.getMessageList();
            //会话id 第一次提问不需要传，之后的多轮对话传入sessionId 本SDK自动帮你返回聊天记录历史消息，一个小时内有效
            String sessionId = data.getSessionId();
            //本次问题的回答，实现自动转为html格式
            String answer = data.getAnswer();
        } catch (NoApiKeyException | InputRequiredException | ClientInputValidationException e) {
            //这里可以记录日志
            log.error("请求大模型接口失败", e);
            return ClientResponse.errorRequest("请求大模型接口失败");
        }
        return ClientResponse.success(data);
    }

    /**
     * 流式传输
     * @param fastChatRequestDto
     * @return
     */
    public ClientResponse<ClientOutput> sseAsk(fastChatRequestDto fastChatRequestDto) {
        //modelSourceSSE流式传输或普通传输
        //requestId请求id，用于区分每次请求 实现幂等
        ClientInput clientInput = ClientInput.builder()
                .modelSource(ModelSource.DASHSCOPE_SSE)
                .requestId(UUID.randomUUID().toString().replace("-", ""))
                .build();
        BeanUtils.copyProperties(fastChatRequestDto, clientInput);
        SseEmitter sseEmitter = new SseEmitter();
        ClientOutput data = null;
        try {
            ClientResponse<Object> call = DyChatClient.call(clientInput);
             data = (ClientOutput) call.getData();
            sseEmitter = data.getSseEmitter();
        } catch (NoApiKeyException | InputRequiredException e) {
            log.error("请求大模型接口失败", e);
            sseEmitter.completeWithError(e);
        } catch (ClientInputValidationException e) {
            throw new RuntimeException(e);
        }
        return ClientResponse.success(data);
    }

}
