package mo.must.chat.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "custom")
@Component
public class CustomConf {

    private Long workId;
    private Long dataCenterId;
}
