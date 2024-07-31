package com.dongye.lxs.chat.service;

import com.dongye.lxs.chat.manager.ChatHistoryManager;
import com.dongye.lxs.chat.bean.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    private final ChatHistoryManager chatHistoryManager;

    @Autowired
    public ChatService(ChatHistoryManager chatHistoryManager) {
        this.chatHistoryManager = chatHistoryManager;
    }

    public List<Message> getChatHistory(String sessionId) {
        return chatHistoryManager.getMessages(sessionId);
    }

    public void addMessageToChatHistory(String sessionId, Message message) {

        chatHistoryManager.createOrUpdateChatRecord(sessionId, message);
    }
}