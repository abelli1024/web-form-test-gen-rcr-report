package mo.must.chat.manager;

import com.alibaba.fastjson.JSON;
import mo.must.chat.business.ReplicateService;
import mo.must.chat.common.config.LlmConfigProperties;
import mo.must.chat.common.util.SystemUtil;
import mo.must.chat.manager.domain.replicatellama.ReplicateLlamaMetricsResponseParameter;
import mo.must.chat.manager.domain.replicatellama.ReplicateLlamaResponseParameter;
import mo.must.chat.model.ReplicateDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReplicateLlamaManager {

    private final ReplicateService replicateService;

    public ReplicateDO sendLlamaAnswer(LlmConfigProperties.LlmConfig llmConfig, CloseableHttpClient client, String question) {
        HttpPost httpPost = new HttpPost(llmConfig.getUrl());
        Map<String,Map<String,String>>params=new HashMap<>();
        Map<String,String>input=new HashMap<>();
        input.put("prompt",question);
        params.put("input",input);
        String request = JSON.toJSONString(params);
        HttpEntity httpEntity = new StringEntity(request, StandardCharsets.UTF_8);
        ;
        httpPost.setEntity(httpEntity);
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Token " + llmConfig.getApiKey());
        RequestConfig config = RequestConfig
                .custom()
                .build();
        httpPost.setConfig(config);
        try {
            return client.execute(httpPost, response -> {
                String resStr = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                ReplicateLlamaResponseParameter responseParameter = JSON.parseObject(resStr, ReplicateLlamaResponseParameter.class);//objectMapper.readValue(resStr, ChatGptResponseParameter.class);
                if (responseParameter != null) {
                    ReplicateDO replicateDO = new ReplicateDO().setLinkId(SystemUtil.getId())
                            .setId(responseParameter.getId())
                            .setModel(responseParameter.getModel())
                            .setVersion(responseParameter.getVersion())
                            .setInput(JSON.toJSONString(responseParameter.getInput()))
                            .setLogs(responseParameter.getLogs())
                            .setError(responseParameter.getError())
                            .setStatus(responseParameter.getStatus())
                            .setCreatedAt(responseParameter.getCreatedAt())
                            .setStartedAt(responseParameter.getStartedAt())
                            .setCompletedAt(responseParameter.getCompletedAt())
                            .setUrlsCancel(responseParameter.getUrls().getCancel())
                            .setUrlsGet(responseParameter.getUrls().getGet());
                    ReplicateLlamaMetricsResponseParameter metrics = responseParameter.getMetrics();
                    if (metrics != null) {
                        replicateDO.setMetricsInputTokenCount(metrics.getInputTokenCount())
                                .setMetricsOutputTokenCount(metrics.getOutputTokenCount())
                                .setMetricsPredictTime(metrics.getPredictTime())
                                .setMetricsTimeToFirstToken(metrics.getTimeToFirstToken())
                                .setMetricsTokensPerSecond(metrics.getTokensPerSecond());
                    }
                    if (responseParameter.getStatus().equals("succeeded")) {
                        StringBuilder sb = new StringBuilder();
                        for (String item : responseParameter.getOutput()) {
                            sb.append(item);
                        }
                        replicateDO.setOutput(sb.toString());
                    }
                    replicateService.saveReplicate(replicateDO);
                    return replicateDO;
                }
                return null;
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ReplicateDO getLlamaAnswer(LlmConfigProperties.LlmConfig llmConfig, CloseableHttpClient client, ReplicateDO replicateDO) {
        HttpGet httpGet = new HttpGet(replicateDO.getUrlsGet());
        httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Token " + llmConfig.getApiKey());
        RequestConfig config = RequestConfig
                .custom()
                .build();
        httpGet.setConfig(config);
        try {
            return client.execute(httpGet, response -> {
                String resStr = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                ReplicateLlamaResponseParameter responseParameter = JSON.parseObject(resStr, ReplicateLlamaResponseParameter.class);//objectMapper.readValue(resStr, ChatGptResponseParameter.class);
                if (responseParameter != null) {
                    replicateDO.setLogs(responseParameter.getLogs())
                            .setError(responseParameter.getError())
                            .setStatus(responseParameter.getStatus())
                            .setCreatedAt(responseParameter.getCreatedAt())
                            .setStartedAt(responseParameter.getStartedAt())
                            .setCompletedAt(responseParameter.getCompletedAt());
                    ReplicateLlamaMetricsResponseParameter metrics = responseParameter.getMetrics();
                    if (metrics != null) {
                        replicateDO.setMetricsInputTokenCount(metrics.getInputTokenCount())
                                .setMetricsOutputTokenCount(metrics.getOutputTokenCount())
                                .setMetricsPredictTime(metrics.getPredictTime())
                                .setMetricsTimeToFirstToken(metrics.getTimeToFirstToken())
                                .setMetricsTokensPerSecond(metrics.getTokensPerSecond());
                    }
                    if (responseParameter.getStatus().equals("succeeded")) {
                        StringBuilder sb = new StringBuilder();
                        for (String item : responseParameter.getOutput()) {
                            sb.append(item);
                        }
                        replicateDO.setOutput(sb.toString());
                    }
                    replicateService.updateReplicate(replicateDO);
                    return replicateDO;
                }
                return null;
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
