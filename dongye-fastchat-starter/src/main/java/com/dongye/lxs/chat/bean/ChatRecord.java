package com.dongye.lxs.chat.bean;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class ChatRecord {
    private String sessionId;
    private List<Message> messages;

    private final LocalDateTime expiryTime;

    public ChatRecord(String sessionId, List<Message> messages, LocalDateTime expiryTime) {
        this.sessionId = sessionId;
        this.messages = messages;
        this.expiryTime = expiryTime;
    }

}