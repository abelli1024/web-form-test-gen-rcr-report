package mo.must.chat.common.framework;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessageManager {

    private  final MessageSource messageSource;

    public String getChinaMessage(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        Locale locale = Locale.CHINA;
        return messageSource.getMessage(code, null, locale);
    }
}
