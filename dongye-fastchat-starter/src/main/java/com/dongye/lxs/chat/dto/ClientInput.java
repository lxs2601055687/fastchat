package com.dongye.lxs.chat.dto;

import com.dongye.lxs.chat.bean.ChatContext;
import com.dongye.lxs.chat.constant.ModelSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Description:   客户端输入参数封装
 * &#064;Date:   2024/7/17 16:00
 * &#064;Author:   李祥生
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientInput {
    /**
     * 请求唯一Id
     */
    private String requestId;
    /**
     * 本次会话的用户id
     */
    private String userId;
    /**
     * 本次会话的用户name
     */
    private String userName;
    /**
     * 本次会话的会话id
     * 用于标识不同的会话
     */
    private String sessionId;
    /**
     * 使用的模型
     */
    private ModelSource modelSource;

    /**
     * 本次会话的上下文
     * json格式
     */
    private ChatContext context;

    /**
     * 本次会话的问题
     */
    private String question;
}
