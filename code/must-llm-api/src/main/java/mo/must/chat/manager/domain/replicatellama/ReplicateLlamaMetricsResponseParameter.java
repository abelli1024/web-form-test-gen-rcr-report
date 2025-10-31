package mo.must.chat.manager.domain.replicatellama;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ReplicateLlamaMetricsResponseParameter {
    private Integer inputTokenCount;
    private Integer outputTokenCount;
    private BigDecimal predictTime;
    private BigDecimal timeToFirstToken;
    private BigDecimal tokensPerSecond;

}
