package com.dongye.lxs.chat.strategy;

import com.dongye.lxs.chat.constant.Protocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class StrategyFactory {
    private static StrategyFactory instance;
    private final ApplicationContext applicationContext;

    @Autowired
    public StrategyFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    private void init() {
        instance = this;
    }

    public static StrategyFactory getInstance() {
        return instance;
    }

    public <T> SendStrategy<T> createStrategy(Protocol protocol) {
        switch (protocol) {
            case Normal:
                return (SendStrategy<T>) applicationContext.getBean(NormalStrategy.class);
            case SSE:
                return (SendStrategy<T>) applicationContext.getBean(SSEStrategy.class);
            default:
                throw new IllegalArgumentException("Unsupported protocol: " + protocol);
        }
    }
}
