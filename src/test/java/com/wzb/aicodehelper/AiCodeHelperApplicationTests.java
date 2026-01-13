package com.wzb.aicodehelper;

import com.wzb.aicodehelper.ai.AiCodeHelper;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;


@SpringBootTest
class AiCodeHelperApplicationTests {

    @Resource
    private AiCodeHelper aiCodeHelper;

    @Test
    void contextLoads() {
    }

    @Test
    void chat() {
        aiCodeHelper.chat("你好，我是小明，喜欢java后端编程。");
    }
    @Test
    void chatWithMessage() {
        try {
            // 将图片 URL 转换为 Base64
            String base64Image = downloadAndConvertImageToBase64("https://www.codefather.cn/logo.png");

            // 创建 ImageContent 使用 Base64 数据
            ImageContent imageContent = ImageContent.from(base64Image, "image/png");

            UserMessage userMessage = UserMessage.from(
                    TextContent.from("描述图片"),
                    imageContent
            );

            aiCodeHelper.chatWithMessage(userMessage);
        } catch (Exception e) {
            System.err.println("Error processing image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 下载图片并转换为 Base64 编码
     */
    private String downloadAndConvertImageToBase64(String imageUrl) throws Exception {
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000); // 5秒连接超时
        connection.setReadTimeout(10000);   // 10秒读取超时

        try (InputStream inputStream = connection.getInputStream()) {
            byte[] imageBytes = inputStream.readAllBytes();
            return Base64.getEncoder().encodeToString(imageBytes);
        } finally {
            connection.disconnect();
        }
    }


}
