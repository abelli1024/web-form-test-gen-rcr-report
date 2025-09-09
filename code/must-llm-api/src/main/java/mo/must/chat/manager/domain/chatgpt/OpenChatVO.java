package mo.must.chat.manager.domain.chatgpt;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class OpenChatVO {
    private Long id;
    private String chatToken;
    private String modelName;
    private Long sessionId;
    private String question;
    private String answer;
    private BigDecimal time;
}
