package mo.must.chat.dto;

import lombok.Data;

@Data
public class ChatItemDTO {
    private Long preId;
    private Long sessionId;
    private Integer pageSize;
}
