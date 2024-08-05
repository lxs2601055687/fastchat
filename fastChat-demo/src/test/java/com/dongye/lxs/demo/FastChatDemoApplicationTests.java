package com.dongye.lxs.demo;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.dongye.lxs.chat.client.DyChatClient;
import com.dongye.lxs.chat.constant.ModelSource;
import com.dongye.lxs.chat.dto.ClientInput;
import com.dongye.lxs.chat.dto.ClientOutput;
import com.dongye.lxs.chat.dto.ClientResponse;
import com.dongye.lxs.chat.exception.ClientInputValidationException;
import com.dongye.lxs.chat.manager.SseEmitterStore;
import io.netty.util.internal.ObjectUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@SpringBootTest
class FastChatDemoApplicationTests {
    /**
     * 测试单个问题
     * @throws NoApiKeyException
     * @throws InputRequiredException
     */
    @Test
    void testOne() throws NoApiKeyException, InputRequiredException, ClientInputValidationException {
        ClientInput clientInput = ClientInput.builder()
                .requestId(UUID.randomUUID().toString())
                .modelSource(ModelSource.DASHSCOPE_Normal)
                .userId("1")
                .userName("李祥生")
                .question("介绍一下海南大学")
                .build();
        System.out.println(DyChatClient.call(clientInput));
    }
    /**
     * 测试多轮对话
     */
    @Test
    void testSecond() throws NoApiKeyException, InputRequiredException, ClientInputValidationException {
        ClientInput clientInput = ClientInput.builder()
                .requestId(UUID.randomUUID().toString())
                .modelSource(ModelSource.DASHSCOPE_Normal)
                .userId("1")
                .userName("李祥生")
                .question("介绍一下海南大学")
                .build();
        ClientResponse<Object> call = DyChatClient.call(clientInput);
        System.out.println(call);
        ClientOutput data = (ClientOutput) call.getData();
        ClientInput clientInput2 = ClientInput.builder()
                .requestId(UUID.randomUUID().toString())
                .modelSource(ModelSource.DASHSCOPE_Normal)
                .userId("1")
                .userName("李祥生")
                .question("有哪些学院")
                .sessionId(data.getSessionId())
                .build();
        System.out.println("---------------------------------");
        System.out.println(DyChatClient.call(clientInput2));
    }
    /**
     * 测试SSE连接
     */
    @Test
      void testSseOne() throws NoApiKeyException, InputRequiredException, InterruptedException, ClientInputValidationException {
        ClientInput clientInput = ClientInput.builder()
                .requestId(UUID.randomUUID().toString())
                .modelSource(ModelSource.DASHSCOPE_SSE)
                .userId("1")
                .userName("李祥生")
                .question("给出C++和java写hello world的代码")
                .build();

        ClientResponse<Object> call = DyChatClient.call(clientInput);
        System.out.println(call);
        ClientOutput data = (ClientOutput) call.getData();
        SseEmitter sseEmitter = SseEmitterStore.getEmitter(data.getSessionId());
        // 创建一个线程池来处理事件
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(5);
            executor.setMaxPoolSize(10);
            executor.setQueueCapacity(25);
            executor.initialize();

            AtomicBoolean completed = new AtomicBoolean(false);
            sseEmitter.onCompletion(() -> completed.set(true));
            sseEmitter.onError(throwable -> System.err.println("Error: " + throwable.getMessage()));
            sseEmitter.onTimeout(() -> System.err.println("Timeout"));

            // 启动监听
            executor.submit(() -> {
                try {
                    sseEmitter.send(SseEmitter.event().name("start").data("Starting..."));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // 模拟处理接收到的事件数据
            // 假设有一个方法 handleEventData(String data) 来处理事件数据
            executor.submit(() -> {
                while (!completed.get()) {
                    try {
                        // 模拟接收数据
                        String eventData = receiveEventData(); // 这是你需要实现的接收事件数据的方法
                        if (eventData != null) {
                            handleEventData(eventData);
                        }
                        TimeUnit.SECONDS.sleep(1); // 等待一秒后检查是否有新数据
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // 恢复线程的中断状态
                        throw new RuntimeException(e);
                    }
                }
            });

            // 等待完成或超时
            while (!completed.get()) {
                try {
                    TimeUnit.SECONDS.sleep(1); // 等待一秒后检查是否完成
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // 恢复线程的中断状态
                    throw e;
                }
            }

            // 关闭线程池
            executor.shutdown();
        }


    // 模拟接收事件数据的方法（需根据具体实现进行修改）
    private static String receiveEventData() {
        // 实现从SSE流接收数据的逻辑
        return null; // 返回接收到的数据
    }

    // 处理接收到的事件数据
    private static void handleEventData(String data) {
        System.out.println("Received data: " + data);
    }


}
