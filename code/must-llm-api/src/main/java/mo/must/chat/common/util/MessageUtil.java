package mo.must.chat.common.util;


import mo.must.chat.common.framework.MessageManager;

public class MessageUtil {

    public static MessageManager messageManager = SpringContextUtil.getBean(MessageManager.class);


    public static String getMessage(String code) {
        return messageManager.getChinaMessage(code);
    }
}
