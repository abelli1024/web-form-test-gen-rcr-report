package mo.must.chat.manager;

import lombok.extern.slf4j.Slf4j;
import mo.must.chat.common.config.LlmConfigProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LlmConfigManager {

    private final Map<String, LlmConfigProperties.LlmConfig> configMap;

    public LlmConfigManager(LlmConfigProperties properties) {
        this.configMap = properties.getConfigs()
                .stream()
                .collect(Collectors.toMap(
                        LlmConfigProperties.LlmConfig::getModel,
                        config -> config
                ));
    }

    public Optional<LlmConfigProperties.LlmConfig> getConfigByModel(String model) {
        return Optional.ofNullable(configMap.get(model));
    }
}
