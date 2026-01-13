package com.wzb.aicodehelper.ai;


import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.output.Response;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Arrays;

/**
 * @author wzb
 */
@Service
@Slf4j
public class AiCodeHelper {

    @Resource
    private ChatModel chatModel;

    @Resource
    private StreamingChatModel streamingChatModel;

    private static final String SYSTEM_MESSAGE = """
            你是ai编程小助手，帮助用户解答编程学习和求职面试相关问题，并且给出建议，重点关注4个方向：
            1.规划清晰的编程学习路线
            2.提供项目学习建议
            3.给出程序员求职全流程指南
            4.分析面试技巧
            请用简洁的语言回答。
            """;
    // 简单对话
    public String chat(String message) {
        SystemMessage systemMessage = SystemMessage.from(SYSTEM_MESSAGE);
        UserMessage userMessage = UserMessage.from(message);
        // 使用 generate 方法
        ChatResponse chatResponse = chatModel.chat(systemMessage, userMessage);
        AiMessage aiMessage = chatResponse.aiMessage();
        // 获取响应中的文本
        String text = aiMessage.text();
        log.info("AI输出: {}", text);
        return text;
    }

    // 自定义用户消息
    public String chatWithMessage(UserMessage userMessage) {
        // 使用 generate 方法
        ChatResponse chatResponse = chatModel.chat(userMessage);
        AiMessage aiMessage = chatResponse.aiMessage();
        // 获取响应中的文本
        String text = aiMessage.text();
        log.info("AI输出: {}", text);
        return text;
    }

    // 流式对话 - 逐字输出
    public Flux<String> chatStream(String message) {
        SystemMessage systemMessage = SystemMessage.from(SYSTEM_MESSAGE);
        UserMessage userMessage = UserMessage.from(message);

        // 创建一个 Sink 来发送流式数据
        Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();

        // 创建消息列表
        java.util.List<ChatMessage> messages = Arrays.asList(systemMessage, userMessage);

        // 使用 StreamingChatModel 进行流式对话
        streamingChatModel.chat(messages, new StreamingChatResponseHandler() {
            @Override
            public void onPartialResponse(String partialResponse) {
                // 每次收到部分响应时，发送到 sink
                log.debug("收到token: {}", partialResponse);
                sink.tryEmitNext(partialResponse);
            }

            @Override
            public void onCompleteResponse(ChatResponse response) {
                // 完成时关闭 sink
                log.info("流式对话完成");
                sink.tryEmitComplete();
            }

            @Override
            public void onError(Throwable error) {
                // 错误时发送错误信号
                log.error("流式对话出错", error);
                sink.tryEmitError(error);
            }
        });

        return sink.asFlux();
    }

}
