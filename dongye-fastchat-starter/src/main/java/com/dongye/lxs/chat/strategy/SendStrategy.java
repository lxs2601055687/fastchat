package com.dongye.lxs.chat.strategy;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.dongye.lxs.chat.dto.ClientInput;
import com.dongye.lxs.chat.dto.ClientResponse;
import org.springframework.stereotype.Component;

@Component
public interface SendStrategy<T> {
    ClientResponse<T> send(ClientInput clientInput) throws NoApiKeyException, InputRequiredException;
}