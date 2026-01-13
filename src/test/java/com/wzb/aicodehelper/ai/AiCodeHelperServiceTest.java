package com.wzb.aicodehelper.ai;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author wzb
 */
@SpringBootTest
class AiCodeHelperServiceTest {

    @Resource
    private AiCodeHelperService aiCodeHelperService;

    @Test
    void chat() {
        String chat = aiCodeHelperService.chat("你好");
        System.out.println(chat);
    }

    @Test
    void chatWithMemory() {
        String chat = aiCodeHelperService.chat("你好,我是EA明");
        System.out.println(chat);
        chat = aiCodeHelperService.chat("我是谁");
        System.out.println(chat);
    }

    @Test
    void chatForReport() {
        String userMessage = "你好，我是程序员小明，学习编程两年半";
        AiCodeHelperService.Report report = aiCodeHelperService.chatForReport(userMessage);
        System.out.println(report);
    }
}