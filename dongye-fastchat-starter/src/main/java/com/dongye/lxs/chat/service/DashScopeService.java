package com.dongye.lxs.chat.service;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.dongye.lxs.chat.dto.ClientInput;
import com.dongye.lxs.chat.dto.ClientOutput;
import com.dongye.lxs.chat.dto.ClientResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public interface DashScopeService {
     ClientResponse<ClientOutput> normalAsk(ClientInput clientInput) throws NoApiKeyException, InputRequiredException;

    ClientResponse<ClientOutput> sseAsk(ClientInput clientInput);
}
