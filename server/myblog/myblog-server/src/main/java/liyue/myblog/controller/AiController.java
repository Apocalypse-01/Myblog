package liyue.myblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import liyue.myblog.dto.AiRequestDTO;
import liyue.myblog.dto.AiResponseDTO;
import liyue.myblog.result.Result;
import liyue.myblog.service.AiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@Tag(name = "AI 辅助写作模块")
@Slf4j
public class AiController {

    @Autowired
    AiService aiService;

    @PostMapping("/assist")
    @Operation(summary = "AI 辅助写作（续写/摘要/标题优化/错别字检查）")
    public Result<AiResponseDTO> aiAssist(@RequestBody AiRequestDTO request) {
        log.info("AI 辅助请求 - action: {}, content length: {}",
                request.getAction(),
                request.getContent() != null ? request.getContent().length() : 0);

        AiResponseDTO response = aiService.handle(request);
        return Result.success(response);
    }
}
