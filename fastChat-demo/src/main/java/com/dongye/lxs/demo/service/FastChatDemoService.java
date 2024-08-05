package com.dongye.lxs.demo.service;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.dongye.lxs.chat.bean.ChatContext;
import com.dongye.lxs.chat.client.DyChatClient;
import com.dongye.lxs.chat.constant.ModelSource;
import com.dongye.lxs.chat.dto.ClientInput;
import com.dongye.lxs.chat.dto.ClientOutput;
import com.dongye.lxs.chat.dto.ClientResponse;
import com.dongye.lxs.chat.exception.ClientInputValidationException;
import com.dongye.lxs.demo.Dto.fastChatRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class FastChatDemoService {

    /**
     * 普通询问，非流式传输
     * @param fastChatRequestDto 请求参数
     * @return 响应结果
     */
    public ClientResponse<ClientOutput> normalAsk(fastChatRequestDto fastChatRequestDto) {
        ClientInput clientInput = createClientInput(fastChatRequestDto, ModelSource.DASHSCOPE_Normal);

        ClientOutput data;
        try {
            ClientResponse<Object> response = DyChatClient.call(clientInput);
            data = (ClientOutput) response.getData();
            logChatContext(data.getContext());
        } catch (NoApiKeyException | InputRequiredException | ClientInputValidationException e) {
            log.error("请求大模型接口失败", e);
            return ClientResponse.errorRequest("请求大模型接口失败");
        }
        return ClientResponse.success(data);
    }

    /**
     * 流式传输
     * @param fastChatRequestDto 请求参数
     * @return 响应结果
     */
    public ClientResponse<ClientOutput> sseAsk(fastChatRequestDto fastChatRequestDto) {
        ClientInput clientInput = createClientInput(fastChatRequestDto, ModelSource.DASHSCOPE_SSE);
        ClientOutput data;
        try {
            ClientResponse<Object> response = DyChatClient.call(clientInput);
            data = (ClientOutput) response.getData();
            logChatContext(data.getContext());
            String sseUrl = data.getSseUrl();
            if (sseUrl == null) {
                log.error("回调错误，sseUrl为空");
                return ClientResponse.errorRequest("sseUrl is null");
            }
        } catch (NoApiKeyException | InputRequiredException | ClientInputValidationException e) {
            log.error("请求大模型接口失败", e);
            return ClientResponse.errorRequest("请求大模型接口失败");
        }
        return ClientResponse.success(data);
    }

    /**
     * 创建ClientInput对象
     * @param fastChatRequestDto 请求参数
     * @param modelSource 模型来源
     * @return ClientInput对象
     */
    private ClientInput createClientInput(fastChatRequestDto fastChatRequestDto, ModelSource modelSource) {
        ClientInput clientInput = ClientInput.builder()
                .modelSource(modelSource)
                .requestId(UUID.randomUUID().toString().replace("-", ""))
                .build();
        BeanUtils.copyProperties(fastChatRequestDto, clientInput);
        return clientInput;
    }

    /**
     * 记录聊天上下文信息
     * @param context 聊天上下文
     */
    private void logChatContext(ChatContext context) {
        if (context != null) {
            log.info("context: {}", context);
            log.info("summary: {}", context.getSummary());
            log.info("messageList: {}", context.getMessageList());
        }
    }
}
