package mo.must.chat.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Table(name = "t_replicate")
@Accessors(chain = true)
public class ReplicateDO {
    private Long linkId;
    @Id
    private String id;

    private String model;
    private String version;
    private String input;
    private String logs;
    private String output;
    private String error;
    private String status;
    private String createdAt;
    private String startedAt;
    private String completedAt;
    private String urlsCancel;
    private String urlsGet;
    private Integer metricsInputTokenCount;
    private Integer metricsOutputTokenCount;
    private BigDecimal metricsPredictTime;
    private BigDecimal metricsTimeToFirstToken;
    private BigDecimal metricsTokensPerSecond;
}
