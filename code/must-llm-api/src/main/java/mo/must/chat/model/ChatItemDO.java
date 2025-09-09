package mo.must.chat.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;


@Data
@Table(name = "t_chat_item")
@Accessors(chain = true)
public class ChatItemDO {
    @Id
    private Long id;
    private String chatToken;
    private String modelName;
    private Long sessionId;
    /**
     * Status 0 Normal 1 Delete
     */
    private Integer status;
    private Date createdTime;
    private Date updateTime;

    private String question;
    private String answer;
    private BigDecimal time;
}
