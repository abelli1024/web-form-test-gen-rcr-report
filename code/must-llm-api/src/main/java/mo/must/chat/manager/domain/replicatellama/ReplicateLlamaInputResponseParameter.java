package mo.must.chat.manager.domain.replicatellama;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ReplicateLlamaInputResponseParameter {
    private String prompt;
}
