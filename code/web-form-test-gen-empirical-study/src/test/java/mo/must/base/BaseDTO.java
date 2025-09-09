package mo.must.base;

import lombok.Data;
import mo.must.processor.HtmlWaiterProcessor;

import java.util.List;
import java.util.Map;

@Data
public class BaseDTO {
    private String webName;
    private String formName;
    private String formTitle;
    private String formSource;
    private String chatModel;

    private Map<String, String> submitBtnRequiredAttrs;
    private String submitBtText;

    private HtmlWaiterProcessor.WaitType waitType;
    private List<String> waitLocators;
    private String waitExtra;
}
