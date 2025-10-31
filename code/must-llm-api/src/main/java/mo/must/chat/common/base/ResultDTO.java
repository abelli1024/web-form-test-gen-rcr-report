package mo.must.chat.common.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultDTO<T> implements Serializable {

    public static final String ERRCODE_NONE = "0";
    /**
     * true: success
     * false: fail
     */
    private boolean success = true;

    private String code = ERRCODE_NONE;

    protected String msg;

    protected T data;
}
