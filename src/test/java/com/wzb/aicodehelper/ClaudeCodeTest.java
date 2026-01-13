package com.wzb.aicodehelper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * @author wzb
 */

@SpringBootTest
class ClaudeCodeTest {

    @Test
    void testConnection() {
        // 1. 测试是否能连接到转发服务
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
                    "https://pmpjfbhq.cn-nb1.rainapp.top",
                    String.class
            );
            System.out.println("转发服务状态: " + response.getStatusCode());
        } catch (Exception e) {
            System.err.println("无法连接到转发服务: " + e.getMessage());
        }

        // 2. 测试 API 调用
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", "sk-fr4oh6eYUeIiW1p7ZQFgJA5y13flLGQIAq7I2LJbCsIQ2Bb7");
        headers.set("Content-Type", "application/json");

        Map<String, Object> body = Map.of(
                "model", "claude-3-5-haiku-20241022",
                "max_tokens", 100,
                "messages", List.of(
                        Map.of("role", "user", "content", "Hello, test")
                )
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://pmpjfbhq.cn-nb1.rainapp.top/v1/messages",
                    request,
                    Map.class
            );
            System.out.println("API 测试结果: " + response.getBody());
        } catch (Exception e) {
            System.err.println("API 调用失败: " + e.getMessage());
        }
    }
}
