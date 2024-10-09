package com.dongye.lxs.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SpringBootApplication
@Import(com.dongye.lxs.chat.config.fastChatConfig.class)
public class FastChatDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastChatDemoApplication.class, args);
    }

}
