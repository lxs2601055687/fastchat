package com.dongye.lxs.chat.utils;

import com.alibaba.dashscope.app.ApplicationResult;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.regex.Pattern;

/**
 * &#064;Description:   消息封装工具类
 * &#064;Date:   2024/7/27 13:00
 * &#064;Author:   李祥生
 */
public final class ContextUtil {

    public static void handleGenerationSseResult(ApplicationResult result, SseEmitter emitter) {
        try {
            String markdown = String.valueOf(result.getOutput().getText()); // 修改这里获取消息内容的方式
            String target = MarkdownConverter.convertMarkdownToHtml(markdown);// 修改这里将消息内容转换为 HTML 的方式
            System.out.println(target);
            emitter.send(SseEmitter.event().data(markdown));
        } catch (Exception e) {
            emitter.completeWithError(e);
        }
    }


}
