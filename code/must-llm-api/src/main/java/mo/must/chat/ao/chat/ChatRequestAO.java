package mo.must.chat.ao.chat;

import lombok.Data;

@Data
public class ChatRequestAO {
    private Integer type;
    private Long sessionId;
    private String msg;
    private String model;
}
