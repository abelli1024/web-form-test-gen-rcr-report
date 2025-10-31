package mo.must.chat.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "llm")
public class LlmConfigProperties {

    private List<LlmConfig> configs = new ArrayList<>();

    @Data
    public static class LlmConfig {
        private String appid;
        private String model;
        private String apiKey;
        private String apiSecret;
        private String url;
    }
}
