package liyue.myblog.service;

import liyue.myblog.dto.AiRequestDTO;
import liyue.myblog.dto.AiResponseDTO;

public interface AiService {

    AiResponseDTO handle(AiRequestDTO request);
}
