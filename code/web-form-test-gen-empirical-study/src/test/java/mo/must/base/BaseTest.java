package mo.must.base;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import mo.must.processor.*;
import net.lightbody.bmp.BrowserMobProxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;


public class BaseTest extends CommonTest {

    public static void println(String logFormat, Object... logValue) {
        LogPrintProcessor.print(logFormat, logValue);
    }


    public static void initBasicBaseDTO(BaseDTO baseDTO, String webName) {
        baseDTO.setWebName(webName);
    }

    public static void initWebBaseDTO(BaseDTO baseDTO, String formTitle, String chatModel) {
        baseDTO.setFormTitle(formTitle);
        baseDTO.setChatModel(chatModel);
    }

    public static void initFormBaseDTO(BaseDTO baseDTO, String formSource) {
        baseDTO.setFormSource(formSource);
    }


    public static SeleniumCom initWebDriver(String url, Map<String, String> headers) {
        return SeleniumProcessor.initWebDriver(BaseTestConstants.CHROME_DRIVER_PATH, url, headers);
    }

    public static SeleniumCom initWebDriver(String url, String cookies) {
        return SeleniumProcessor.initWebDriver(BaseTestConstants.CHROME_DRIVER_PATH, url, cookies);
    }

    public static WebDriverWait initWebDriverWait(ChromeDriver driver, long seconds) {
        return SeleniumProcessor.initWebDriverWait(driver, seconds);
    }

    public static void quitWebDriver(SeleniumCom seleniumCom) {
        BrowserMobProxy proxy = seleniumCom.getProxy();
        SeleniumProcessor.stopBrowserMobProxy(proxy);
        ChromeDriver driver = seleniumCom.getDriver();
        SeleniumProcessor.quitWebDriver(driver);
    }

    public static String getFormHtml(ChromeDriver driver) {
        String formHtml = SeleniumProcessor.getFormHtml(driver);
        return HtmlCompressor.compressHtml(formHtml);
    }

    public static String getSimplifiedFormHtml(ChromeDriver driver) {
        Set<String> tagsToRemove = new HashSet<>();
        Set<String> attributesToKeep = new HashSet<>(Arrays.asList("id", "name", "type", "placeholder", "src", "disabled", "readonly", "value", "checked", "required", "selected"));
        Set<String> typesToRemove = new HashSet<>();
        String simplifiedFormHtml = SeleniumProcessor.executeFormScript(driver, tagsToRemove, attributesToKeep, typesToRemove);
        String compressHtml = HtmlCompressor.compressHtml(simplifiedFormHtml);
        return HtmlCompressor.compressHtml(compressHtml);
    }

    public static void fillForm(ChromeDriver driver, List<HtmlFormElement> formElements, Map<String, String> chatResponseMap) {
        println("begin fill form");
        SeleniumProcessor.fillForm(driver, formElements, chatResponseMap);
        println("end fill form");
    }

    public static void submitForm(ChromeDriver driver, HtmlFormSubmitButton btn) {
        println("begin submit form, element: {}", JSON.toJSONString(btn));
        SeleniumProcessor.submitForm(driver, btn);
        println("end submit form");
    }

    public static boolean waitResponse(WebDriver driver, BaseDTO baseDTO) {
        if (baseDTO.getWaitType() == null) {
            println("waitResponse,waitType is null");
            return false;
        }
        boolean waitedResponse = SeleniumProcessor.waitResponse(driver, baseDTO.getWaitType(), baseDTO.getWaitLocators(), baseDTO.getWaitExtra());
        println("waitedResponse:{}", waitedResponse);
        return waitedResponse;
    }


    private static void dealValidStyle0(ChromeDriver driver, WebDriverWait wait, BaseDTO baseDTO) {
        String webTitle = SeleniumProcessor.getWebTitle(driver);
        initWebBaseDTO(baseDTO, SeleniumProcessor.getWebTitle(driver), BaseTestConstants.CHAT_MODEL);
        String sFormHtml = getFormHtml(driver);

        String formHtml = getSimplifiedFormHtml(driver);
        println("formHtml:{}", formHtml);
        List<HtmlFormElement> formElements = HtmlCompressor.parseHtmlFormElement(formHtml);
        println("formElements:{}", JSON.toJSONString(formElements));

        Integer elementNum = CollectionUtil.isEmpty(formElements) ? 0 : formElements.size();
        String includeContent = buildHtmlFormElementInclude(formElements);
        println("includeContent:{}", includeContent);

        String generatePrompt = PromptProcessor.generateStyle1ValidPrompt(baseDTO.getWebName(), elementNum, webTitle, includeContent, formHtml);
        println("generatePrompt:{}", generatePrompt);

        String chat = chat(BaseTestConstants.CHAT_MODEL, generatePrompt);
        println("chat:{}", chat);

        Map<String, String> chatResponseMap = ChatProcessor.processMustChatResponse(chat);
        println("chatResponseMap:{}", JSON.toJSONString(chatResponseMap));
        initFormBaseDTO(baseDTO, formHtml);
        boolean result = false;
        try {
            fillForm(driver, formElements, chatResponseMap);
            HtmlFormSubmitButton htmlFormSubmitButton = HtmlCompressor.parseHtmlFormSubmitButtonElement(sFormHtml, baseDTO.getSubmitBtnRequiredAttrs(), baseDTO.getSubmitBtText());
            submitForm(driver, htmlFormSubmitButton);
            result = waitResponse(driver, baseDTO);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            saveStyleWebGuiRecord(0, result, baseDTO);
        }
    }

    private static void dealValidStyle1(ChromeDriver driver, WebDriverWait wait, BaseDTO baseDTO) {
        String webTitle = SeleniumProcessor.getWebTitle(driver);
        initWebBaseDTO(baseDTO, webTitle, BaseTestConstants.CHAT_MODEL);
        String sFormHtml = getFormHtml(driver);
        String formHtml = getSimplifiedFormHtml(driver);
        println("formHtml:{}", formHtml);
        List<HtmlFormElement> formElements = HtmlCompressor.parseHtmlFormElement(formHtml);
        println("formElements:{}", JSON.toJSONString(formElements));

        String generateStyle2StructPrompt = PromptProcessor.generateStyle2StructPrompt(formHtml);
        println("generateStyle2StructPrompt:{}", generateStyle2StructPrompt);
        String structChat = chat(BaseTestConstants.CHAT_MODEL, generateStyle2StructPrompt);
        println("struct chat:{}", structChat);
        List<ChatStruct> chatStructList = ChatProcessor.processMustChatStructResponse(structChat);
        String taskContent = processChatStructTaskContent(chatStructList);
        println("taskContent:{}", taskContent);
        Integer elementNum = CollectionUtil.isEmpty(formElements) ? 0 : formElements.size();
        String includeContent = buildChatStructInclude(chatStructList);
        println("includeContent:{}", includeContent);

        String generatePrompt = PromptProcessor.generateStyle2ValidPrompt(baseDTO.getWebName(), elementNum, webTitle, includeContent, taskContent);
        println("generatePrompt:{}", generatePrompt);
        String chat = chat(BaseTestConstants.CHAT_MODEL, generatePrompt);
        println("chat:{}", chat);

        Map<String, String> chatResponseMap = ChatProcessor.processMustChatResponse(chat);
        println("chatResponseMap:{}", JSON.toJSONString(chatResponseMap));
        initFormBaseDTO(baseDTO, formHtml);
        boolean result = false;
        try {
            fillForm(driver, formElements, chatResponseMap);
            HtmlFormSubmitButton htmlFormSubmitButton = HtmlCompressor.parseHtmlFormSubmitButtonElement(sFormHtml, baseDTO.getSubmitBtnRequiredAttrs(), baseDTO.getSubmitBtText());
            submitForm(driver, htmlFormSubmitButton);
            result = waitResponse(driver, baseDTO);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            saveStyleWebGuiRecord(1, result, baseDTO);
        }
    }

    private static void dealValidStyle2(ChromeDriver driver, WebDriverWait wait, BaseDTO baseDTO) {
        String webTitle = SeleniumProcessor.getWebTitle(driver);
        initWebBaseDTO(baseDTO, webTitle, BaseTestConstants.CHAT_MODEL);
        String sFormHtml = getFormHtml(driver);

        String formHtml = getSimplifiedFormHtml(driver);
        println("formHtml:{}", formHtml);
        List<HtmlFormElement> formElements = HtmlCompressor.parseHtmlFormElement(formHtml);
        println("formElements:{}", JSON.toJSONString(formElements));

        String taskContent = processHtmlFormElementTaskContent(formElements);
        println("taskContent:{}", taskContent);
        Integer elementNum = CollectionUtil.isEmpty(formElements) ? 0 : formElements.size();
        String includeContent = buildHtmlFormElementInclude(formElements);
        println("includeContent:{}", includeContent);

        String generatePrompt = PromptProcessor.generateStyle2ValidPrompt(baseDTO.getWebName(), elementNum, webTitle, includeContent, taskContent);
        println("generatePrompt:{}", generatePrompt);
        String chat = chat(BaseTestConstants.CHAT_MODEL, generatePrompt);
        println("chat:{}", chat);

        Map<String, String> chatResponseMap = ChatProcessor.processMustChatResponse(chat);
        println("chatResponseMap:{}", JSON.toJSONString(chatResponseMap));
        initFormBaseDTO(baseDTO, formHtml);
        boolean result = false;
        try {
            fillForm(driver, formElements, chatResponseMap);
            HtmlFormSubmitButton htmlFormSubmitButton = HtmlCompressor.parseHtmlFormSubmitButtonElement(sFormHtml, baseDTO.getSubmitBtnRequiredAttrs(), baseDTO.getSubmitBtText());
            submitForm(driver, htmlFormSubmitButton);
            result = waitResponse(driver, baseDTO);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            saveStyleWebGuiRecord(2, result, baseDTO);
        }
    }


    public static void dealTestValidStyle0(BaseDTO baseDTO, String url) {
        Map<String, String> headers = new HashMap<>();
        dealTestValidStyle0(baseDTO, url, headers);
    }

    public static void dealTestValidStyle0(BaseDTO baseDTO, String url, Map<String, String> headers) {
        SeleniumCom seleniumCom = initWebDriver(url, headers);
        ChromeDriver driver = seleniumCom.getDriver();
        WebDriverWait wait = initWebDriverWait(driver, 10L);
        dealValidStyle0(driver, wait, baseDTO);
        quitWebDriver(seleniumCom);
    }

    public static void dealTestValidStyle0(BaseDTO baseDTO, String url, String cookies) {
        SeleniumCom seleniumCom = initWebDriver(url, cookies);
        ChromeDriver driver = seleniumCom.getDriver();
        WebDriverWait wait = initWebDriverWait(driver, 10L);
        dealValidStyle0(driver, wait, baseDTO);
        quitWebDriver(seleniumCom);
    }


    public static void dealTestValidStyle1(BaseDTO baseDTO, String url) {
        Map<String, String> headers = new HashMap<>();
        dealTestValidStyle1(baseDTO, url, headers);
    }

    public static void dealTestValidStyle1(BaseDTO baseDTO, String url, Map<String, String> headers) {
        SeleniumCom seleniumCom = initWebDriver(url, headers);
        ChromeDriver driver = seleniumCom.getDriver();
        WebDriverWait wait = initWebDriverWait(driver, 10L);
        dealValidStyle1(driver, wait, baseDTO);
        quitWebDriver(seleniumCom);
    }


    public static void dealTestValidStyle1(BaseDTO baseDTO, String url, String cookies) {
        SeleniumCom seleniumCom = initWebDriver(url, cookies);
        ChromeDriver driver = seleniumCom.getDriver();
        WebDriverWait wait = initWebDriverWait(driver, 10L);
        dealValidStyle1(driver, wait, baseDTO);
        quitWebDriver(seleniumCom);
    }


    public static void dealTestValidStyle2(BaseDTO baseDTO, String url) {
        Map<String, String> headers = new HashMap<>();
        dealTestValidStyle2(baseDTO, url, headers);
    }

    public static void dealTestValidStyle2(BaseDTO baseDTO, String url, Map<String, String> headers) {
        SeleniumCom seleniumCom = initWebDriver(url, headers);
        ChromeDriver driver = seleniumCom.getDriver();
        WebDriverWait wait = initWebDriverWait(driver, 10L);
        dealValidStyle2(driver, wait, baseDTO);
        quitWebDriver(seleniumCom);
    }

    public static void dealTestValidStyle2(BaseDTO baseDTO, String url, String cookies) {
        SeleniumCom seleniumCom = initWebDriver(url, cookies);
        ChromeDriver driver = seleniumCom.getDriver();
        WebDriverWait wait = initWebDriverWait(driver, 10L);
        dealValidStyle2(driver, wait, baseDTO);
        quitWebDriver(seleniumCom);
    }
}
