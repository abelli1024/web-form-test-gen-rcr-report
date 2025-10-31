package mo.must.chat.manager;


import com.alibaba.fastjson.JSON;
import mo.must.chat.common.config.LlmConfigProperties;
import mo.must.chat.common.util.HttpUtil;
import mo.must.chat.manager.domain.chatgpt.ChatGptMessage;
import mo.must.chat.manager.domain.chatgpt.ChatGptRequestParameter;
import mo.must.chat.manager.domain.chatgpt.ChatGptResponseParameter;
import mo.must.chat.manager.domain.chatgpt.Choices;
import okhttp3.*;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.List;

public class CustomChatGpt {

    private ChatGptRequestParameter chatGptRequestParameter = new ChatGptRequestParameter();
    private int responseTimeout = 1000;

    public void setResponseTimeout(int responseTimeout) {
        this.responseTimeout = responseTimeout;
    }

    public String getAnswer(LlmConfigProperties.LlmConfig llmConfig, CloseableHttpClient client, String question) {
        try {
            chatGptRequestParameter.addModel(llmConfig.getModel());
            chatGptRequestParameter.addMessages(new ChatGptMessage("user", question));
            ChatGptResponseParameter responseParameter = dealGPT(llmConfig);
            String ans = "";
            for (Choices choice : responseParameter.getChoices()) {
                ChatGptMessage message = choice.getMessage();
                chatGptRequestParameter.addMessages(new ChatGptMessage(message.getRole(), message.getContent()));
                String s = message.getContent().replaceAll("\n+", "\n");
                ans += s;
            }
            return ans;
        } catch (Exception e) {
            e.printStackTrace();
        }
        chatGptRequestParameter.getMessages().remove(chatGptRequestParameter.getMessages().size() - 1);
        return "Your current network is not accessible";
    }

    public String getAnswer(LlmConfigProperties.LlmConfig llmConfig, CloseableHttpClient client, List<ChatGptMessage> messages) {
        try {
            chatGptRequestParameter.addModel(llmConfig.getModel());
            chatGptRequestParameter.addMessages(messages);
            ChatGptResponseParameter responseParameter = dealGPT(llmConfig);
            String ans = "";
            for (Choices choice : responseParameter.getChoices()) {
                ChatGptMessage message = choice.getMessage();
                chatGptRequestParameter.addMessages(new ChatGptMessage(message.getRole(), message.getContent()));
                String s = message.getContent().replaceAll("\n+", "\n");
                ans += s;
            }
            return ans;
        } catch (Exception e) {
            e.printStackTrace();
        }
        chatGptRequestParameter.getMessages().remove(chatGptRequestParameter.getMessages().size() - 1);
        return "Your current network is not accessible";
    }

    public ChatGptResponseParameter dealGPT(LlmConfigProperties.LlmConfig llmConfig) throws IOException {
        OkHttpClient client = HttpUtil.createUnsafeOkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(chatGptRequestParameter));
        Request request = new Request.Builder()
                .url(llmConfig.getUrl())
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer "+llmConfig.getApiKey())
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String rsp = response.body().string();
            return JSON.parseObject(rsp,ChatGptResponseParameter.class);
        }
        return null;
    }
}

