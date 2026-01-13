package com.wzb.aicodehelper.ai;

import dev.langchain4j.service.SystemMessage;

import java.util.List;

/**
 * @author wzb
 */

public interface AiCodeHelperService {
    @SystemMessage("system-prompt.txt")
    String chat(String userMessage);

    @SystemMessage("system-prompt.txt")
    Report chatForReport(String userMessage);

    // 学习报告
    record Report(String name, List<String> suggestionList){}
}
