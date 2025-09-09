package mo.must.base;


import java.util.HashMap;
import java.util.Map;

public class BaseTestConstants {
    public static final String CHROME_DRIVER_PATH = "chromedriver/chromedriver";
    public static final String CHAT_MODEL = "gpt-4";

    public static final Map<String, String> CHAT_MODEL_MAP = new HashMap<>();

    static {
        CHAT_MODEL_MAP.put("gpt-3.5-turbo",   "gpt_3.5");
        CHAT_MODEL_MAP.put("gpt-4",           "gpt4");
        CHAT_MODEL_MAP.put("Baichuan2-Turbo", "Baichuan2");
        CHAT_MODEL_MAP.put("Spark_v3",        "Spark_v3");
        CHAT_MODEL_MAP.put("Spark_v3.5",      "Spark_v3.5");
        CHAT_MODEL_MAP.put("glm-4",           "glm_4");
        CHAT_MODEL_MAP.put("glm-4v",          "glm_4v");
        CHAT_MODEL_MAP.put("glm-3-turbo",     "glm_3");
        CHAT_MODEL_MAP.put("llama-2-7b-chat",  "llama2_7b");
        CHAT_MODEL_MAP.put("llama-2-13b-chat", "llama2_13b");
        CHAT_MODEL_MAP.put("llama-2-70b-chat", "llama2_70b");
    }
}
