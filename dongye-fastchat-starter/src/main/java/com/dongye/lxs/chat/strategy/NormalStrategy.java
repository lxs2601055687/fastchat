package com.dongye.lxs.chat.strategy;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.dongye.lxs.chat.constant.ModelSource;
import com.dongye.lxs.chat.dto.ClientInput;
import com.dongye.lxs.chat.dto.ClientOutput;
import com.dongye.lxs.chat.dto.ClientResponse;
import com.dongye.lxs.chat.service.DashScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class NormalStrategy implements SendStrategy<ClientOutput> {

    @Autowired
    private DashScopeService dashScopeService;
    @Override
    public ClientResponse<ClientOutput> send(ClientInput clientInput) throws NoApiKeyException, InputRequiredException {
            // 判断模型
        if(clientInput.getModelSource() == ModelSource.DASHSCOPE_Normal) {
            //阿里通义
            return dashScopeService.normalAsk(clientInput);
        }
       /* if(clientInput.getModelSource() == ModelSource.SPARKMAX_WEBSOCKET) {
            //讯飞星火
            return null;
        }*/
        return ClientResponse.errorRequest("Unsupported model source: " + clientInput.getModelSource());
    }
}