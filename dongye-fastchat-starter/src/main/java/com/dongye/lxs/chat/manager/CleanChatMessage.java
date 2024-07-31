package com.dongye.lxs.chat.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Component
public class CleanChatMessage {
    private final ExpiryCleaner expiryCleaner;

    @Autowired
    public CleanChatMessage(ExpiryCleaner expiryCleaner) {
        this.expiryCleaner = expiryCleaner;
    }

    @PostConstruct
    public void init() {
        expiryCleaner.startCleaningTask(0, 1, TimeUnit.MINUTES); // 启动时立即执行一次，然后每分钟执行一次
    }

    @PreDestroy
    public void cleanup() {
        expiryCleaner.shutdown();
    }
}
