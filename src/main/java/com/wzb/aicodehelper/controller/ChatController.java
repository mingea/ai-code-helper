package com.wzb.aicodehelper.controller;

import com.wzb.aicodehelper.ai.AiCodeHelper;
import com.wzb.aicodehelper.dto.ChatRequest;
import com.wzb.aicodehelper.dto.ChatResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * AI编程小助手聊天接口
 * @author wzb
 */
@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
@Slf4j
public class ChatController {

    @Resource
    private AiCodeHelper aiCodeHelper;

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        log.info("收到用户消息: {}", request.getMessage());
        String response = aiCodeHelper.chat(request.getMessage());
        return new ChatResponse(response);
    }

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@RequestBody ChatRequest request) {
        log.info("收到用户流式消息: {}", request.getMessage());
        return aiCodeHelper.chatStream(request.getMessage());
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
