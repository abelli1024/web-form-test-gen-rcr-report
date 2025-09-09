package mo.must.base;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import mo.must.processor.*;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;


public class CommonTest {
    public static void println(String logFormat, Object... logValue) {
        LogPrintProcessor.print(logFormat, logValue);
    }

    public static String buildHtmlFormElementInclude(List<HtmlFormElement> formElements) {
        if (CollectionUtil.isEmpty(formElements)) {
            return "";
        }
        Map<String, Integer> countMap = new HashMap<>();
        for (HtmlFormElement formElement : formElements) {
            String key = formElement.getType() + "_" + formElement.getTag();
            Integer count = countMap.get(key);
            countMap.put(key, count == null ? 1 : count + 1);
        }
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            String[] keyArr = StringUtils.split(entry.getKey(), "_");
            if (keyArr.length <= 0) {
                continue;
            }
            String item = entry.getValue() + " " + keyArr[0] + " " + (keyArr.length >= 2 ? keyArr[1] : "");
            list.add(item);
        }
        String inlcludeStr = "";
        for (int i = 0; i < list.size(); i++) {
            inlcludeStr += (i == list.size() - 1) ? list.get(i) : list.get(i) + " and ";
        }
        return inlcludeStr;
    }

    public static String buildChatStructInclude(List<ChatStruct> chatStructList) {
        if (CollectionUtil.isEmpty(chatStructList)) {
            return "";
        }
        Map<String, Integer> countMap = new HashMap<>();
        for (ChatStruct chatStruct : chatStructList) {
            String key = chatStruct.getInputType() + "_" + chatStruct.getElementType();
            Integer count = countMap.get(key);
            countMap.put(key, count == null ? 1 : count + 1);
        }
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            String[] keyArr = StringUtils.split(entry.getKey(), "_");
            if (keyArr.length <= 0) {
                continue;
            }
            String item = entry.getValue() + " " + keyArr[0] + " " + (keyArr.length >= 2 ? keyArr[1] : "");
            list.add(item);
        }
        String inlcludeStr = "";
        for (int i = 0; i < list.size(); i++) {
            inlcludeStr += (i == list.size() - 1) ? list.get(i) : list.get(i) + " and ";
        }
        return inlcludeStr;
    }

    public static String chat(String model, String content) {
        println("begin chat");
        String chat = ChatProcessor.chat(model, content);
        println("end chat");
        return chat;
    }

    public static String numberToOrdinal(int number) {
        if (number <= 0) {
            return "Invalid number";
        }
        switch (number % 100) {
            case 11:
            case 12:
            case 13:
                return number + "th";
            default:
                switch (number % 10) {
                    case 1:
                        return number + "st";
                    case 2:
                        return number + "nd";
                    case 3:
                        return number + "rd";
                    default:
                        return number + "th";
                }
        }
    }

    public static String processChatStructTaskContent(List<ChatStruct> chatStructList) {
        StringBuilder sb = new StringBuilder();
        try {
            for (int i = 0; i < chatStructList.size(); i++) {
                ChatStruct chatStruct = chatStructList.get(i);
                String inputId = chatStruct.getInputId();
                String elementType = chatStruct.getElementType();
                String inputType = chatStruct.getInputType();
                String placeholder = chatStruct.getPlaceholder();
                String inputValue = chatStruct.getInputValue();
                String inputName = chatStruct.getInputName();
                if (StringUtils.isBlank(inputId) && StringUtils.isBlank(inputName)) {
                    continue;
                }

                sb.append("For the ").append(numberToOrdinal((i + 1))).append(" ");
                sb.append(inputType).append(" ").append(elementType).append(",").append(" ");
                if (!StringUtils.isBlank(placeholder)) {
                    sb.append("the hint text is ").append(placeholder).append(",").append(" ");
                }
                if (!StringUtils.isBlank(inputId)) {
                    sb.append("and the element id is ").append("``").append(inputId).append("``.").append(" ");
                }
                if (StringUtils.isBlank(inputId) && !StringUtils.isBlank(inputName)) {
                    sb.append("and the element name is ").append("``").append(inputName).append("``.").append(" ");
                }
                if (!StringUtils.isBlank(inputValue)) {
                    sb.append("and the element default value is ").append("``").append(inputValue).append("``.").append(" ");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String processHtmlFormElementTaskContent(List<HtmlFormElement> formElements) {
        StringBuilder sb = new StringBuilder();
        try {
            for (int i = 0; i < formElements.size(); i++) {
                HtmlFormElement chatStruct = formElements.get(i);
                String inputId = chatStruct.getId();
                String elementType = chatStruct.getTag();
                String inputType = chatStruct.getType();
                String placeholder = chatStruct.getPlaceholder();
                String inputValue = chatStruct.getValue();
                String inputName = chatStruct.getName();
                if (StringUtils.isBlank(inputId) && StringUtils.isBlank(inputName)) {
                    continue;
                }
                sb.append("For the ").append(numberToOrdinal((i + 1))).append(" ");
                sb.append(inputType).append(" ").append(elementType).append(",").append(" ");
                if (!StringUtils.isBlank(placeholder)) {
                    sb.append("the hint text is ").append(placeholder).append(",").append(" ");
                }
                if (!StringUtils.isBlank(inputId)) {
                    sb.append("and the element id is ").append("``").append(inputId).append("``.").append(" ");
                }
                if (StringUtils.isBlank(inputId) && !StringUtils.isBlank(inputName)) {
                    sb.append("and the element name is ").append("``").append(inputName).append("``.").append(" ");
                }
                if (!StringUtils.isBlank(inputValue)) {
                    sb.append("and the element default value is ").append("``").append(inputValue).append("``.").append(" ");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private static final Path OUT_DIR = Paths.get("output", "webgui");

    private static int nextIndex(Path dir, String baseName) {
        int max = 0;
        String glob = baseName + "-*.json";
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(dir, glob)) {
            for (Path p : ds) {
                String fn = p.getFileName().toString();
                int dash = fn.lastIndexOf('-');
                int dot = fn.lastIndexOf('.');
                if (dash > 0 && dot > dash) {
                    String num = fn.substring(dash + 1, dot);
                    try {
                        int n = Integer.parseInt(num);
                        if (n > max) max = n;
                    } catch (NumberFormatException ignore) {
                    }
                }
            }
        } catch (IOException ignore) {
        }
        return max + 1;
    }

    private static String safe(String s) {
        String x = (s == null || s.trim().isEmpty()) ? "unknown" : s.trim();
        return x.replaceAll("[\\\\/:*?\"<>|]", "_");
    }

    public static void saveStyleWebGuiRecord(Integer style, boolean result, BaseDTO dto) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("style", style);
        body.put("webName", dto.getWebName());
        body.put("formName", dto.getFormName());
        body.put("formTitle", dto.getFormTitle());
        body.put("chatModel", dto.getChatModel());
        body.put("result", result ? 1 : 0);

        try {
            Files.createDirectories(OUT_DIR);
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }

        String model = BaseTestConstants.CHAT_MODEL_MAP.get(dto.getChatModel());
        String baseName = String.join("-", safe(dto.getWebName()), safe(dto.getFormName()), safe(model), safe(String.valueOf(style)));

        int next = nextIndex(OUT_DIR, baseName);

        while (true) {
            Path file = OUT_DIR.resolve(baseName + "-" + next + ".json");
            try (BufferedWriter w = Files.newBufferedWriter(file, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW)) {
                w.write(JSON.toJSONString(body, true));
                System.out.println("saved: " + file.toAbsolutePath());
                break;
            } catch (FileAlreadyExistsException e) {
                next++;
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("save failed: " + e.getMessage());
                break;
            }
        }
        println("saveStyleWebGuiRecord, result:{}", JSON.toJSONString(body));
        println("--------------------------分割线------------------------------");
    }
}
