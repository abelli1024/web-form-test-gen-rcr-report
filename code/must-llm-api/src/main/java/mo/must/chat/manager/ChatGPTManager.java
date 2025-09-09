package mo.must.chat.manager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mo.must.chat.business.ChatItemService;
import mo.must.chat.common.config.LlmConfigProperties;
import mo.must.chat.common.util.SystemUtil;
import mo.must.chat.manager.domain.chatgpt.ChatGptMessage;
import mo.must.chat.manager.domain.chatgpt.OpenChatVO;
import mo.must.chat.model.ChatItemDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatGPTManager {
    private final ChatGPTAsyncManager chatGPTAsyncManager;
    private final ChatItemService chatItemService;
    private final SparkManager sparkManager;
    private final GLMManager glmManager;
    private final ReplicateManager replicateManager;

    public OpenChatVO chat(LlmConfigProperties.LlmConfig llmConfig, Integer type, Long sessionId, String question) throws InterruptedException {
        long id = SystemUtil.getId();
        if (StringUtils.isBlank(question)) {
            return new OpenChatVO().setId(id).setModelName(llmConfig.getModel()).setSessionId(id).setQuestion(question).setAnswer("none").setTime(BigDecimal.ZERO);
        }

        if (llmConfig.getModel().equals("Spark_v3") || llmConfig.getModel().equals("Spark_v3.5")) {
            return sparkManager.chat(llmConfig, type, sessionId, question);
        }
        if (llmConfig.getModel().equals("glm-4") || llmConfig.getModel().equals("glm-4v") || llmConfig.getModel().equals("glm-3-turbo")) {
            return glmManager.chat(llmConfig, type, sessionId, question);
        }

        if (llmConfig.getModel().equals("llama-2-7b-chat") || llmConfig.getModel().equals("llama-2-13b-chat") || llmConfig.getModel().equals("llama-2-70b-chat")) {
            return replicateManager.chatLlama(llmConfig, type, sessionId, question);
        }
        if (type != null && type == 1) {
            OpenChatVO openChatVO = new OpenChatVO().setId(id).setChatToken("-").setModelName(llmConfig.getModel()).setSessionId(sessionId == null ? id : sessionId).setQuestion(question).setAnswer("-").setTime(BigDecimal.ZERO);
            chatGPTAsyncManager.asyncChatGpt(llmConfig, sessionId, openChatVO);
            return openChatVO;
        }
        return sync(llmConfig, sessionId, question);
    }

    public OpenChatVO sync(LlmConfigProperties.LlmConfig llmConfig, Long sessionId, String question) {
        List<ChatGptMessage> messages = new ArrayList<>();
        if (sessionId != null) {
            List<ChatItemDO> chatItems = chatItemService.getChatItems(sessionId);
            List<ChatItemDO> subbedList = chatItems.subList(0, chatItems.size() >= 10 ? 10 : chatItems.size());
            for (ChatItemDO chatItemDO : subbedList) {
                messages.add(new ChatGptMessage("user", chatItemDO.getQuestion()));
                messages.add(new ChatGptMessage("assistant", chatItemDO.getAnswer()));
            }
            messages.add(new ChatGptMessage("user", question));
        } else {
            messages.add(new ChatGptMessage("user", question));
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CustomChatGpt customChatGpt = new CustomChatGpt();
        customChatGpt.setResponseTimeout(200000);
        long start = System.currentTimeMillis();
        String answer = customChatGpt.getAnswer(llmConfig, httpClient, messages);
        long end = System.currentTimeMillis();
        long id = SystemUtil.getId();
        OpenChatVO openChatVO = new OpenChatVO().setId(id).setChatToken("-").setModelName(llmConfig.getModel()).setSessionId(sessionId == null ? id : sessionId).setQuestion(question).setAnswer(answer).setTime(BigDecimal.valueOf((end - start) / 1000.0));
        try {
            httpClient.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        chatGPTAsyncManager.saveChatItem(openChatVO);
        return openChatVO;
    }
}
