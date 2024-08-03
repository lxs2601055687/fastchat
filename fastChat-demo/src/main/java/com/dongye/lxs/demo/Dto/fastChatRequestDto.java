package com.dongye.lxs.demo.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class fastChatRequestDto {
    /**
     * 会话Id
     */
    private String sessionId;

    /**
     * 用户Id
     */
    private String userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 问题
     */
    private String question;

}
