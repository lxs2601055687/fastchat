package com.dongye.lxs.chat.service;

import com.alibaba.dashscope.app.*;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.dongye.lxs.chat.bean.ChatContext;
import com.dongye.lxs.chat.bean.Message;
import com.dongye.lxs.chat.bean.SummaryRep;
import com.dongye.lxs.chat.constant.MessageSource;
import com.dongye.lxs.chat.dto.ClientInput;
import com.dongye.lxs.chat.dto.ClientOutput;
import com.dongye.lxs.chat.dto.ClientResponse;
import com.dongye.lxs.chat.manager.ChatHistoryManager;
import com.dongye.lxs.chat.utils.ContextUtil;
import com.dongye.lxs.chat.utils.MarkdownConverter;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.HashMap;

import static com.dongye.lxs.chat.utils.MarkdownConverter.convertMarkdownToHtml;

@Service
public class DashScopeNormalServiceImpl implements DashScopeService{

    private final ApplicationParam applicationParam;

    private final ChatHistoryManager chatHistoryManager;

     @Autowired
    public DashScopeNormalServiceImpl(ApplicationParam applicationParam, ChatHistoryManager chatHistoryManager) {
        this.applicationParam = applicationParam;
         this.chatHistoryManager = chatHistoryManager;
     }

    /**
     * 多轮对话
     * @param clientInput
     * @return
     */
    @Override
    public ClientResponse<ClientOutput> normalAsk(ClientInput clientInput) {
        SummaryRep summaryRep = new SummaryRep();
        String sessionId = clientInput.getSessionId();
        String question = clientInput.getQuestion();
        if (sessionId == null) {
            //认为是第一次对话，生成总结
             summaryRep = generateSummary(question);
             sessionId = summaryRep.getSessionId();
        }
        setupApplicationParam(sessionId, question);
           //生成回答
        ApplicationResult applicationResult = callApplication();
        if (applicationResult == null) {
            return ClientResponse.errorRequest("请求大模型接口失败");
        }
        String summary = summaryRep.getSummary() == null ? "" : summaryRep.getSummary();
        return buildClientResponse(sessionId, question,  summary , applicationResult);
    }

    @Override
    public ClientResponse<SseEmitter> sseAsk(ClientInput clientInput) {
        SseEmitter emitter = new SseEmitter();
        SummaryRep summaryRep = null;
        String sessionId = clientInput.getSessionId();
        String question = clientInput.getQuestion();
        if (sessionId == null) {
            //认为是第一次对话，生成总结
            summaryRep = generateSummary(question);
            sessionId = summaryRep.getSessionId();
        }
        setupApplicationParam(sessionId, question);
        String summary = summaryRep.getSummary();
        Flowable<ApplicationResult> applicationResultFlowable = callApplicationWithSse();
        Disposable disposable = applicationResultFlowable.subscribe(result -> {
            try {
                ContextUtil.handleGenerationSseResult(result, emitter);
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
        emitter.onCompletion(disposable::dispose); // 取消订阅流
        emitter.onTimeout(emitter::complete); // 处理超时情况
        return ClientResponse.success(emitter);
    }

    private void setupApplicationParam(String sessionId, String question) {
        applicationParam.setPrompt(question);
        if (sessionId != null) {
            applicationParam.setSessionId(sessionId);
        }
    }

    private SummaryRep generateSummary(String question) {
        try {
            return getSummary(question);
        } catch (NoApiKeyException | InputRequiredException e) {
            throw new RuntimeException(e);
        }
    }

    private ApplicationResult callApplication() {
        try {
            Application application = new Application();
            return application.call(applicationParam);
        } catch (NoApiKeyException | InputRequiredException e) {
            return null;
        }
    }
    private  Flowable<ApplicationResult> callApplicationWithSse() {
        try {
            Application application = new Application();
            return application.streamCall(applicationParam);
        } catch (NoApiKeyException | InputRequiredException e) {
            return null;
        }
    }
    private SummaryRep getSummary(String question) throws NoApiKeyException, InputRequiredException {
        Application application = new Application();
        applicationParam.setPrompt("给下面这段问题来一个20字内的总结？" + question);
        ApplicationResult  summary = application.call(applicationParam);
        return SummaryRep.builder()
                .summary(summary.getOutput().getText())
                .sessionId(summary.getOutput().getSessionId())
                .build();
    }


    private ClientResponse<ClientOutput> buildClientResponse(String sessionId, String question, String summary, ApplicationResult applicationResult) {
        ApplicationOutput output = applicationResult.getOutput();
        String result = convertMarkdownToHtml(output.getText());
        saveHistoryMessage(sessionId, question, result);

        ChatContext context = ChatContext.builder()
                .messageList(chatHistoryManager.getMessages(sessionId))
                .time(LocalDateTime.now())
                .summary(summary)
                .build();

        ClientOutput clientOutput = ClientOutput.builder()
                .sessionId(sessionId)
                .answer(result)
                .context(context)
                .build();

        return ClientResponse.success(clientOutput);
    }
    private void saveHistoryMessage(String sessionId, String question, String result) {
        HashMap<MessageSource,String> resultMap = new HashMap<>();
        Message message = new Message();
        resultMap.put(MessageSource.USER, question);
        resultMap.put(MessageSource.MODEL, result);
        message.setMessageMap(resultMap);
        chatHistoryManager.createOrUpdateChatRecord(sessionId,message);
    }
}
