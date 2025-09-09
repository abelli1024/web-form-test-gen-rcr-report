package mo.must.chat.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mo.must.chat.ao.chat.ChatRequestAO;
import mo.must.chat.common.base.ResultBuilder;
import mo.must.chat.common.config.LlmConfigProperties;
import mo.must.chat.manager.ChatGPTManager;
import mo.must.chat.manager.LlmConfigManager;
import mo.must.chat.manager.domain.chatgpt.OpenChatVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatGPT")
public class ChatAPI {

    private final ChatGPTManager chatGPTManager;
    private final LlmConfigManager llmConfigManager;

    @RequestMapping(value = {"/chat"})
    public Object chat(@RequestBody ChatRequestAO chatRequest) throws Exception {
        LlmConfigProperties.LlmConfig llmConfig = llmConfigManager.getConfigByModel(chatRequest.getModel()).orElse(null);
        if (llmConfig == null) {
            return ResultBuilder.failure("900001", "Please configure llm configuration first");
        }
        log.info("chat begin......");
        OpenChatVO chat = chatGPTManager.chat(llmConfig, chatRequest.getType(), chatRequest.getSessionId(), chatRequest.getMsg());
        log.info("chat end......");
        return ResultBuilder.success(chat);
    }
}
