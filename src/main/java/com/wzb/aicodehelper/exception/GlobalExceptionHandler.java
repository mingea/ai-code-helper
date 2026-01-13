package com.wzb.aicodehelper.exception;

import com.wzb.aicodehelper.dto.ChatResponse;
import dev.langchain4j.exception.HttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * @author wzb
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ChatResponse> handleHttpException(HttpException e) {
        log.error("AI服务调用失败: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ChatResponse("AI服务暂时不可用，请稍后重试。"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ChatResponse> handleException(Exception e) {
        log.error("系统异常: ", e);
        String message = "抱歉，系统发生错误，请稍后重试。";

        // 针对 langchain4j 的异常提供更友好的提示
        if (e.getClass().getName().contains("langchain4j")) {
            message = "AI服务响应超时或不可用，请稍后重试。";
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ChatResponse(message));
    }
}
