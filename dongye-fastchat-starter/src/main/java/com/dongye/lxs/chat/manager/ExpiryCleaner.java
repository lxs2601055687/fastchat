package com.dongye.lxs.chat.manager;

import com.dongye.lxs.chat.bean.ChatRecord;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ExpiryCleaner {
    private final ChatHistoryManager chatHistoryManager;
    private final ScheduledExecutorService scheduler;

    public ExpiryCleaner(ChatHistoryManager chatHistoryManager) {
        this.chatHistoryManager = chatHistoryManager;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void startCleaningTask(long initialDelay, long period, TimeUnit unit) {
        scheduler.scheduleAtFixedRate(this::cleanExpiredRecords, initialDelay, period, unit);
    }

    void cleanExpiredRecords() {
        LocalDateTime now = LocalDateTime.now();
        chatHistoryManager.chatRecords.entrySet().removeIf(entry -> entry.getValue().getExpiryTime().isBefore(now));
    }

    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
