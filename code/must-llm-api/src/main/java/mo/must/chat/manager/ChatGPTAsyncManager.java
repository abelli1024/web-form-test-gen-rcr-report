package mo.must.chat.manager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mo.must.chat.business.ChatItemService;
import mo.must.chat.common.config.LlmConfigProperties;
import mo.must.chat.common.util.DateUtil;
import mo.must.chat.manager.domain.chatgpt.ChatGptMessage;
import mo.must.chat.manager.domain.chatgpt.OpenChatVO;
import mo.must.chat.model.ChatItemDO;
import mo.must.chat.model.ReplicateDO;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatGPTAsyncManager {
    private final ChatItemService chatItemService;
    private final ReplicateLlamaManager replicateLlamaManager;

    @Async
    public void asyncChatGpt(LlmConfigProperties.LlmConfig llmConfig, Long sessionId, OpenChatVO chatVO) {
        List<ChatGptMessage> messages = new ArrayList<>();
        if (sessionId != null) {
            List<ChatItemDO> chatItems = chatItemService.getChatItems(sessionId);
            if (!CollectionUtils.isEmpty(chatItems)) {
                List<ChatItemDO> subbedList = chatItems.subList(0, chatItems.size() >= 10 ? 10 : chatItems.size());
                for (ChatItemDO chatItemDO : subbedList) {
                    messages.add(new ChatGptMessage("user", chatItemDO.getQuestion()));
                    messages.add(new ChatGptMessage("assistant", chatItemDO.getAnswer()));
                }
            }
            messages.add(new ChatGptMessage("user", chatVO.getQuestion()));
        } else {
            messages.add(new ChatGptMessage("user", chatVO.getQuestion()));
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CustomChatGpt customChatGpt = new CustomChatGpt();
        customChatGpt.setResponseTimeout(200000);
        long start = System.currentTimeMillis();
        String answer = customChatGpt.getAnswer(llmConfig, httpClient, messages);
        long end = System.currentTimeMillis();
        chatVO.setAnswer(answer).setTime(BigDecimal.valueOf((end - start) / 1000.0));
        saveChatItem(chatVO);
        try {
            httpClient.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public void asyncReplicateLlama(LlmConfigProperties.LlmConfig llmConfig, Long sessionId, OpenChatVO chatVO) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ReplicateDO replicateDO = replicateLlamaManager.sendLlamaAnswer(llmConfig, httpClient, chatVO.getQuestion());
        if (replicateDO.getStatus().equals("succeeded")) {

        }
        chatVO.setId(replicateDO.getLinkId());
        saveChatItem(chatVO);
    }

    @Async
    public void saveChatItem(OpenChatVO openChatVO) {
        ChatItemDO chatItemDO = buildChatItemDO(openChatVO.getModelName(),openChatVO.getChatToken(), openChatVO.getId(), openChatVO.getSessionId(), openChatVO.getQuestion(), openChatVO.getAnswer(), openChatVO.getTime());
        chatItemService.saveChatItem(chatItemDO);
    }

    private ChatItemDO buildChatItemDO(String modelName,String chatToken, Long id, Long sessionId, String question, String answer, BigDecimal time) {
        return new ChatItemDO()
                .setId(id)
                .setChatToken(chatToken)
                .setModelName(modelName)
                .setSessionId(sessionId)
                .setStatus(0)
                .setCreatedTime(DateUtil.currDate())
                .setUpdateTime(DateUtil.currDate())
                .setQuestion(question)
                .setAnswer(answer)
                .setTime(time);
    }
}
