package com.dongye.lxs.chat.dto;

import com.dongye.lxs.chat.bean.ChatContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ClientOutput {


    private ChatContext context;


    private String answer;


    private String sessionId;

}
