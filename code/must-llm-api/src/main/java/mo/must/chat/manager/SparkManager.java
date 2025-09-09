package mo.must.chat.manager;

import com.unfbx.sparkdesk.SparkDeskClient;
import com.unfbx.sparkdesk.entity.*;
import com.unfbx.sparkdesk.listener.ChatListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mo.must.chat.business.ChatItemService;
import mo.must.chat.common.config.LlmConfigProperties;
import mo.must.chat.common.util.SystemUtil;
import mo.must.chat.manager.domain.chatgpt.OpenChatVO;
import mo.must.chat.model.ChatItemDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SparkManager {
    private final ChatGPTAsyncManager chatGPTAsyncManager;
    private final ChatItemService chatItemService;

    public OpenChatVO chat(LlmConfigProperties.LlmConfig llmConfig, Integer type, Long sessionId, String question) {
        long id = SystemUtil.getId();
        if (StringUtils.isBlank(question)) {
            return new OpenChatVO().setId(id).setChatToken("-").setModelName(llmConfig.getModel()).setSessionId(id).setQuestion(question).setAnswer("none").setTime(BigDecimal.ZERO);
        }
        try {
            return sync(llmConfig, sessionId, question);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public OpenChatVO sync(LlmConfigProperties.LlmConfig llmConfig, Long sessionId, String question) throws InterruptedException {
        List<Text> text = new ArrayList<>();
        if (sessionId != null) {
            List<ChatItemDO> chatItems = chatItemService.getChatItems(sessionId);
            List<ChatItemDO> subbedList = chatItems.subList(0, chatItems.size() >= 10 ? 10 : chatItems.size());
            for (ChatItemDO chatItemDO : subbedList) {
                text.add(Text.builder().role(Text.Role.ASSISTANT.getName()).content(chatItemDO.getAnswer()).build());
            }
            text.add(Text.builder().role(Text.Role.USER.getName()).content(question).build());
        } else {
            text.add(Text.builder().role(Text.Role.USER.getName()).content(question).build());
        }
        long start = System.currentTimeMillis();
        String answer = dealSpark(llmConfig, text);
        long end = System.currentTimeMillis();
        long id = SystemUtil.getId();
        OpenChatVO openChatVO = new OpenChatVO().setId(id).setChatToken("-").setModelName(llmConfig.getModel()).setSessionId(sessionId == null ? id : sessionId).setQuestion(question).setAnswer(answer).setTime(BigDecimal.valueOf((end - start) / 1000.0));
        chatGPTAsyncManager.saveChatItem(openChatVO);
        return openChatVO;
    }

    public String dealSpark(LlmConfigProperties.LlmConfig llmConfig, List<Text> text) throws InterruptedException {
        StringBuilder sb = new StringBuilder();
        SparkDeskClient sparkDeskClient = SparkDeskClient.builder()
                .host(llmConfig.getUrl())
                .appid(llmConfig.getAppid())
                .apiKey(llmConfig.getApiKey())
                .apiSecret(llmConfig.getApiSecret())
                .build();
        InHeader header = InHeader.builder().uid(UUID.randomUUID().toString().substring(0, 10)).appid("0061d454").build();
        Parameter parameter = Parameter.builder().chat(Chat.builder().domain("generalv2").maxTokens(2048).temperature(0.3).build()).build();
        InPayload payload = InPayload.builder().message(Message.builder().text(text).build()).build();
        AIChatRequest aiChatRequest = AIChatRequest.builder().header(header).parameter(parameter).payload(payload).build();
        final boolean[] sparkFlag = {false};
        sparkDeskClient.chat(new ChatListener(aiChatRequest) {
            @Override
            public void onChatError(AIChatResponse aiChatResponse) {
                sparkFlag[0] = true;
                List<Text> text = aiChatResponse.getPayload().getChoices().getText();
                for (Text txt : text) {
                    sb.append(txt.getContent());
                }
            }

            @Override
            public void onChatOutput(AIChatResponse aiChatResponse) {
                List<Text> text = aiChatResponse.getPayload().getChoices().getText();
                for (Text txt : text) {
                    sb.append(txt.getContent());
                }
            }

            @Override
            public void onChatEnd() {
                sparkFlag[0] = true;
                System.out.println("The current session is over");
            }

            @Override
            public void onChatToken(Usage usage) {
                System.out.println("token：" + usage);
            }
        });
        while (true) {
            Thread.sleep(200);
            if (sparkFlag[0]) {
                return sb.toString();
            }
        }
    }
}
