package liyue.myblog.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import liyue.myblog.config.AiConfig;
import liyue.myblog.dto.AiRequestDTO;
import liyue.myblog.dto.AiResponseDTO;
import liyue.myblog.service.AiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AiServiceImpl implements AiService {

    @Autowired
    private AiConfig aiConfig;

    private final RestClient restClient = RestClient.create();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public AiResponseDTO handle(AiRequestDTO request) {
        String systemPrompt = buildSystemPrompt(request.getAction());
        String userMessage = buildUserMessage(request);

        Map<String, Object> requestBody = Map.of(
                "model", aiConfig.getModel(),
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userMessage)
                ),
                "temperature", 0.7,
                "max_tokens", 2000
        );

        try {
            String responseJson = restClient.post()
                    .uri(aiConfig.getUrl())
                    .header("Authorization", "Bearer " + aiConfig.getKey())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            log.info("AI response received, length: {}", responseJson != null ? responseJson.length() : 0);

            Map<String, Object> responseMap = objectMapper.readValue(responseJson, new TypeReference<>() {});
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");
            if (choices == null || choices.isEmpty()) {
                throw new RuntimeException("AI 返回结果为空");
            }
            Map<String, Object> firstChoice = choices.get(0);
            Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
            String result = (String) message.get("content");

            AiResponseDTO response = new AiResponseDTO();
            response.setResult(result.trim());
            return response;

        } catch (Exception e) {
            log.error("AI 接口调用失败", e);
            AiResponseDTO errorResponse = new AiResponseDTO();
            errorResponse.setResult("AI 服务暂时不可用，请稍后重试。");
            return errorResponse;
        }
    }

    private String buildSystemPrompt(String action) {
        return switch (action) {
            case "continue" ->
                    "你是一个专业的写作助手。请根据用户提供的文章内容，自然地续写接下来的段落。" +
                            "续写内容应与原文风格一致，逻辑连贯，不要重复已有内容。只返回续写的内容，不要加任何解释。";
            case "summarize" ->
                    "你是一个文章摘要专家。请用100-200字概括用户提供的文章核心内容。" +
                            "摘要应简洁明了，抓住重点。只返回摘要内容，不要加任何前缀说明。";
            case "polishTitle" ->
                    "你是一个标题优化专家。请根据用户提供的文章内容和原标题，生成3个更有吸引力的标题。" +
                            "标题应简洁有力，能吸引读者点击。每个标题占一行，前面加上序号1. 2. 3.，不要加其他内容。";
            case "checkTypos" ->
                    "你是一个文字校对专家。请检查用户提供的文章内容中的错别字、语法错误和标点问题。" +
                            "如果有错误，逐条列出错误位置和修改建议。如果没有错误，回复'未发现明显错误'。" +
                            "格式：原文「...」→ 建议修改为「...」";
            default ->
                    "你是一个写作助手。请根据用户的需求提供帮助。";
        };
    }

    private String buildUserMessage(AiRequestDTO request) {
        String action = request.getAction();
        String content = request.getContent() != null ? request.getContent() : "";
        String title = request.getTitle() != null ? request.getTitle() : "";

        return switch (action) {
            case "continue" -> "请续写以下文章内容：\n\n" + content;
            case "summarize" -> "请为以下文章生成摘要：\n\n" + content;
            case "polishTitle" -> "原标题：「" + title + "」\n\n文章内容：\n" + content;
            case "checkTypos" -> "请检查以下文章中的错别字：\n\n" + content;
            default -> content;
        };
    }
}
