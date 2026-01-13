package com.wzb.aicodehelper.ai;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wzb
 */
@Configuration
public class AiCodeHelperFactory {
    @Resource
    private ChatModel chatModel;

    @Bean
    public AiCodeHelperService aiCodeHelperService() {
        // 会话记忆
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        AiCodeHelperService aiCodeHelperService = AiServices.builder(AiCodeHelperService.class)
                .chatModel(chatModel)
                .chatMemory(chatMemory)
//                .chatMemoryProvider(memory -> MessageWindowChatMemory.withMaxMessages(10))
                .build();
        return aiCodeHelperService;
    }

}
