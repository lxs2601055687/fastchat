package com.dongye.lxs.chat.manager;

import com.dongye.lxs.chat.bean.ChatRecord;
import com.dongye.lxs.chat.bean.Message;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatHistoryManager {
  protected final Map<String, ChatRecord> chatRecords = new ConcurrentHashMap<>();

    public String generateSessionId() {
        return UUID.randomUUID().toString();
    }

    public ChatRecord getChatRecord(String sessionId) {
        return chatRecords.get(sessionId);
    }

    public void createOrUpdateChatRecord(String sessionId, Message message) {
        if(getChatRecord(sessionId) == null) {
            //新的会话有效期为一个小时过期时间当前时间加一小时
            List<Message> messages = new ArrayList<>();
            messages.add(message);
            chatRecords.put(sessionId, new ChatRecord(sessionId ,messages , LocalDateTime.now().plusHours(1)));
        } else {
            //拿到旧的过期时间
            LocalDateTime expiryTime = getChatRecord(sessionId).getExpiryTime();
            //更新message列表
            List<Message> messages = new ArrayList<>(getMessages(sessionId));
            messages.add(message);
            chatRecords.put(sessionId, new ChatRecord(sessionId, messages, expiryTime));
        }
    }

    public List<Message> getMessages(String sessionId) {
        ChatRecord chatRecord = getChatRecord(sessionId);
        if (chatRecord != null) {
            return chatRecord.getMessages();
        }
        return List.of();
    }

}