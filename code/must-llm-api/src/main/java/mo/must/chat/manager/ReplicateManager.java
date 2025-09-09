package mo.must.chat.manager;

import mo.must.chat.business.ChatItemService;
import mo.must.chat.common.config.LlmConfigProperties;
import mo.must.chat.common.util.DateUtil;
import mo.must.chat.common.util.SystemUtil;
import mo.must.chat.manager.domain.chatgpt.OpenChatVO;
import mo.must.chat.model.ChatItemDO;
import mo.must.chat.model.ReplicateDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReplicateManager {
    private final ChatGPTAsyncManager chatGPTAsyncManager;
    private final ReplicateLlamaManager replicateLlamaManager;
    private final ChatItemService chatItemService;

    public OpenChatVO chatLlama(LlmConfigProperties.LlmConfig llmConfig, Integer type, Long sessionId, String question) throws InterruptedException {
        long id = SystemUtil.getId();
        if (StringUtils.isBlank(question)) {
            return new OpenChatVO().setId(id).setChatToken("-").setModelName(llmConfig.getModel()).setSessionId(id).setQuestion(question).setAnswer("none").setTime(BigDecimal.ZERO);
        }
        if (type != null && type == 1) {
            OpenChatVO openChatVO = new OpenChatVO().setChatToken("-").setId(id).setModelName(llmConfig.getModel()).setSessionId(sessionId == null ? id : sessionId).setQuestion(question).setAnswer("-").setTime(BigDecimal.ZERO);
            chatGPTAsyncManager.asyncReplicateLlama(llmConfig, sessionId, openChatVO);
            return openChatVO;
        }
        return syncLlama(llmConfig, sessionId, question);
    }

    public OpenChatVO syncLlama(LlmConfigProperties.LlmConfig llmConfig, Long sessionId, String question) throws InterruptedException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ReplicateDO replicateDO = replicateLlamaManager.sendLlamaAnswer(llmConfig, httpClient, question);
        OpenChatVO openChatVO = new OpenChatVO().setId(replicateDO.getLinkId()).setChatToken("-").setModelName(llmConfig.getModel()).setSessionId(sessionId == null ? replicateDO.getLinkId() : sessionId).setQuestion(question).setAnswer(replicateDO.getOutput()).setTime(null);
        chatGPTAsyncManager.saveChatItem(openChatVO);
        do {
            Thread.sleep(1000);
            ReplicateDO llamaAnswer = replicateLlamaManager.getLlamaAnswer(llmConfig, httpClient, replicateDO);
            replicateDO.setLogs(llamaAnswer.getLogs())
                    .setError(llamaAnswer.getError())
                    .setStatus(llamaAnswer.getStatus())
                    .setCreatedAt(llamaAnswer.getCreatedAt())
                    .setStartedAt(llamaAnswer.getStartedAt())
                    .setCompletedAt(llamaAnswer.getCompletedAt())
                    .setMetricsInputTokenCount(llamaAnswer.getMetricsInputTokenCount())
                    .setMetricsOutputTokenCount(llamaAnswer.getMetricsOutputTokenCount())
                    .setMetricsPredictTime(llamaAnswer.getMetricsPredictTime())
                    .setMetricsTimeToFirstToken(llamaAnswer.getMetricsTimeToFirstToken())
                    .setMetricsTokensPerSecond(llamaAnswer.getMetricsTokensPerSecond())
                    .setOutput(llamaAnswer.getOutput());

            if (replicateDO.getStatus().equals("succeeded")) {
                long start = DateUtil.utcDateConverter(replicateDO.getStartedAt()).getTime();
                long end = DateUtil.utcDateConverter(replicateDO.getCompletedAt()).getTime();
                ChatItemDO chatItem = chatItemService.getChatItem(replicateDO.getLinkId());
                chatItem.setAnswer(replicateDO.getOutput()).setTime(BigDecimal.valueOf((end - start) / 1000.0));
                chatItemService.updateChatItem(chatItem);
                openChatVO.setAnswer(replicateDO.getOutput()).setTime(chatItem.getTime());
            }
        } while (!replicateDO.getStatus().equals("succeeded") && !replicateDO.getStatus().equals("failed"));
        try {
            httpClient.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return openChatVO;
    }
}
