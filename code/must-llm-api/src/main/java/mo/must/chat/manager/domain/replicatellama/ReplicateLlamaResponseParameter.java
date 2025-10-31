package mo.must.chat.manager.domain.replicatellama;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ReplicateLlamaResponseParameter {

    private String id;
    private String model;
    private String version;
    private ReplicateLlamaInputResponseParameter input;

    private String logs;
    private List<String> output;
    private String error;
    private String status;
    private String createdAt;
    private String startedAt;
    private String completedAt;
    private ReplicateLlamaUrlsResponseParameter urls;
    private ReplicateLlamaMetricsResponseParameter metrics;
}
