package com.dongye.lxs.chat.strategy;

import com.dongye.lxs.chat.constant.ModelSource;
import com.dongye.lxs.chat.dto.ClientInput;
import com.dongye.lxs.chat.dto.ClientOutput;
import com.dongye.lxs.chat.dto.ClientResponse;
import com.dongye.lxs.chat.service.DashScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
@Component
public class SSEStrategy implements SendStrategy<ClientOutput> {
    @Autowired
    private DashScopeService dashScopeService;
    @Override
    public ClientResponse<ClientOutput> send(ClientInput clientInput) {
        // 判断模型
        if(clientInput.getModelSource() == ModelSource.DASHSCOPE_SSE) {
            //阿里通义
            return dashScopeService.sseAsk(clientInput);
        }
        return ClientResponse.errorRequest("Unsupported model source: " + clientInput.getModelSource());
    }
}