package com.dongye.lxs.chat.bean;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * &#064;Description:    聊天上下文
 * &#064;Date:    2021/8/17 15:00
 * &#064;Author:    李祥生
 */
@Data
@Builder
@AllArgsConstructor
public class ChatContext {
    /**
     * 本次会话的问题总结
     */
    private String summary;
    /**
     * 本次会话的消息列表
     */
    private List<Message> messageList;
    /**
     * 消息时间
     */
    private LocalDateTime time;

}
