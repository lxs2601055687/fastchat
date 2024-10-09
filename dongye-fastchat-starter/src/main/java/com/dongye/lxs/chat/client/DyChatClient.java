package com.dongye.lxs.chat.client;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.dongye.lxs.chat.constant.Protocol;
import com.dongye.lxs.chat.dto.ClientInput;
import com.dongye.lxs.chat.dto.ClientResponse;
import com.dongye.lxs.chat.exception.ClientInputValidationException;
import com.dongye.lxs.chat.sentinel.SentinelConstant;
import com.dongye.lxs.chat.strategy.SendStrategy;
import com.dongye.lxs.chat.strategy.StrategyFactory;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * &#064;Description:   fastChat核心类
 * &#064;Author:  李祥生
 * &#064;CreateDate:  2024/7/27 20:00
 */
public final class DyChatClient {

    private static final LoadingCache<String, ClientResponse<?>> cache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(new CacheLoader<>() {
                @NotNull
                @Override
                public ClientResponse<?> load(@NotNull String key) throws Exception {
                    // 该方法不会被调用，因为我们不会通过 cache.get 直接加载数据
                    throw new UnsupportedOperationException("Direct loading is not supported");
                }
            });

    /**
     * 发送消息 SSE or Normal
     * @param clientInput 客户端输入
     * @return 客户端输出
     */

    @SentinelResource(value = SentinelConstant.chatByTongYiSse,
            blockHandler = "handleBlockException",
            fallback = "handleFallback")
    public static <T> ClientResponse<T> call(ClientInput clientInput) throws NoApiKeyException, InputRequiredException, ClientInputValidationException {
        //增加校验参数的方法
        validateClientInput(clientInput);
        String requestId = clientInput.getRequestId();
        ClientResponse<T> response = (ClientResponse<T>) cache.getIfPresent(requestId);
        if (response == null) {
            synchronized (DyChatClient.class) {
                response = (ClientResponse<T>) cache.getIfPresent(requestId);
                if (response == null) {
                    Protocol protocol = clientInput.getModelSource().getProtocol();
                    SendStrategy<T> strategy = StrategyFactory.getInstance().createStrategy(protocol);
                    response = strategy.send(clientInput);
                    cache.put(requestId, response);
                }
            }
        }
        return response;
    }
    public static <T> ClientResponse<T>  handleFallback(ClientInput clientInput){
        return null;
    }
    private static void validateClientInput(ClientInput clientInput) throws ClientInputValidationException {
        if (clientInput == null) {
            throw new ClientInputValidationException("参数未传递");
        }
        if (isNullOrEmpty(clientInput.getRequestId())) {
            throw new ClientInputValidationException("RequestId 不能为空 建议使用UUID");
        }
        if (isNullOrEmpty(clientInput.getUserId())) {
            throw new ClientInputValidationException("UserId 不能为空");
        }
        if (isNullOrEmpty(clientInput.getUserName())) {
            throw new ClientInputValidationException("UserName 不能为空");
        }
        if (clientInput.getModelSource() == null) {
            throw new ClientInputValidationException("ModelSource 不能为空");
        }
        if (isNullOrEmpty(clientInput.getQuestion())) {
            throw new ClientInputValidationException("Question 不能为空");
        }
    }

    private static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
