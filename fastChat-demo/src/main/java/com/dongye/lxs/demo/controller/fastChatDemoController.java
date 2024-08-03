package com.dongye.lxs.demo.controller;

import com.dongye.lxs.chat.dto.ClientInput;
import com.dongye.lxs.chat.dto.ClientOutput;
import com.dongye.lxs.chat.dto.ClientResponse;
import com.dongye.lxs.demo.Dto.fastChatRequestDto;
import com.dongye.lxs.demo.service.fastChatDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class fastChatDemoController {

   @Autowired
    private fastChatDemoService fastChatService;

    @PostMapping("/fastChat/normalAsk")
    public ClientResponse<ClientOutput> normalAsk(@RequestBody fastChatRequestDto fastChatRequestDto) {
        ClientResponse<ClientOutput> clientOutputClientResponse = fastChatService.normalAsk(fastChatRequestDto);
        System.out.println(clientOutputClientResponse);
        return clientOutputClientResponse;
    }

    @PostMapping("/fastChat/sseAsk")
    public ClientResponse<ClientOutput> sseAsk(@RequestBody fastChatRequestDto fastChatRequestDto) {
        return fastChatService.sseAsk(fastChatRequestDto);
    }
}
