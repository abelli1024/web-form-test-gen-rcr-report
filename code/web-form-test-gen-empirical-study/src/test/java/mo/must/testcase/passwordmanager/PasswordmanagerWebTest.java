package mo.must.testcase.passwordmanager;


import mo.must.base.BaseDTO;
import mo.must.base.BaseTest;
import mo.must.processor.HtmlWaiterProcessor;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class PasswordmanagerWebTest extends BaseTest {

    private BaseDTO baseDTO;

    @Before
    public void init() {
        baseDTO = new BaseDTO();
        initBasicBaseDTO(baseDTO, "password_manager_web");
    }

    @Test
    public void testStyle1ValidLogin() {
        String url = "http://127.0.0.1:8080/auth/login";
        baseDTO.setFormName("login_form");
        Map<String, String> submitBtnRequiredAttrs = Map.of("type", "submit", "value", "Войти");
        baseDTO.setSubmitBtnRequiredAttrs(submitBtnRequiredAttrs);
        baseDTO.setSubmitBtText("Войти");
        baseDTO.setWaitType(HtmlWaiterProcessor.WaitType.CONTAINER_TEXT_LIST);
        baseDTO.setWaitLocators(List.of(".error", ".website-div"));
        dealTestValidStyle0(baseDTO, url);
    }


    @Test
    public void testStyle2ValidLogin() {
        String url = "http://127.0.0.1:8080/auth/login";
        baseDTO.setFormName("login_form");
        Map<String, String> submitBtnRequiredAttrs = Map.of("type", "submit", "value", "Войти");
        baseDTO.setSubmitBtnRequiredAttrs(submitBtnRequiredAttrs);
        baseDTO.setSubmitBtText("Войти");
        baseDTO.setWaitType(HtmlWaiterProcessor.WaitType.CONTAINER_TEXT_LIST);
        baseDTO.setWaitLocators(List.of(".error", ".website-div"));
        dealTestValidStyle1(baseDTO, url);
    }


    @Test
    public void testStyle3ValidLogin() {
        String url = "http://127.0.0.1:8080/auth/login";
        baseDTO.setFormName("login_form");
        Map<String, String> submitBtnRequiredAttrs = Map.of("type", "submit", "value", "Войти");
        baseDTO.setSubmitBtnRequiredAttrs(submitBtnRequiredAttrs);
        baseDTO.setSubmitBtText("Войти");
        baseDTO.setWaitType(HtmlWaiterProcessor.WaitType.CONTAINER_TEXT_LIST);
        baseDTO.setWaitLocators(List.of(".error", ".website-div"));
        dealTestValidStyle2(baseDTO, url);
    }


    @Test
    public void testStyle1ValidRegister() {
        String url = "http://127.0.0.1:8080/auth/registration";
        baseDTO.setFormName("register_form");
        Map<String, String> submitBtnRequiredAttrs = Map.of("type", "submit", "value", "Зарегистрироваться!");
        baseDTO.setSubmitBtnRequiredAttrs(submitBtnRequiredAttrs);
        baseDTO.setSubmitBtText("Зарегистрироваться!");
        baseDTO.setWaitType(HtmlWaiterProcessor.WaitType.TEXT);
        baseDTO.setWaitLocators(List.of("Login"));
        dealTestValidStyle0(baseDTO, url);
    }


    @Test
    public void testStyle2ValidRegister() {
        String url = "http://127.0.0.1:8080/auth/registration";
        baseDTO.setFormName("register_form");
        Map<String, String> submitBtnRequiredAttrs = Map.of("type", "submit", "value", "Зарегистрироваться!");
        baseDTO.setSubmitBtnRequiredAttrs(submitBtnRequiredAttrs);
        baseDTO.setSubmitBtText("Зарегистрироваться!");
        baseDTO.setWaitType(HtmlWaiterProcessor.WaitType.TEXT);
        baseDTO.setWaitLocators(List.of("Login"));
        dealTestValidStyle1(baseDTO, url);
    }

    @Test
    public void testStyle3ValidRegister() {
        String url = "http://127.0.0.1:8080/auth/registration";
        baseDTO.setFormName("register_form");
        Map<String, String> submitBtnRequiredAttrs = Map.of("type", "submit", "value", "Зарегистрироваться!");
        baseDTO.setSubmitBtnRequiredAttrs(submitBtnRequiredAttrs);
        baseDTO.setSubmitBtText("Зарегистрироваться!");
        baseDTO.setWaitType(HtmlWaiterProcessor.WaitType.TEXT);
        baseDTO.setWaitLocators(List.of("Login"));
        dealTestValidStyle2(baseDTO, url);
    }



    @Test
    public void testStyle1ValidNewWebInfo() {
        String cookies = "JSESSIONID=D277431DA630D72DF7587F0BB8BC20AA";
        String url = "http://127.0.0.1:8080/web-info/new";
        baseDTO.setFormName("web_info_new_form");
        Map<String, String> submitBtnRequiredAttrs = Map.of("type", "submit", "value", "Добавить");
        baseDTO.setSubmitBtnRequiredAttrs(submitBtnRequiredAttrs);
        baseDTO.setSubmitBtText("Добавить");
        baseDTO.setWaitType(HtmlWaiterProcessor.WaitType.CONTAINER_TEXT_LIST);
        baseDTO.setWaitLocators(List.of(".website-div"));
        dealTestValidStyle0(baseDTO, url, cookies);
    }


    @Test
    public void testStyle2ValidNewWebInfo() {
        String cookies = "JSESSIONID=D277431DA630D72DF7587F0BB8BC20AA";
        String url = "http://127.0.0.1:8080/web-info/new";
        baseDTO.setFormName("web_info_new_form");
        Map<String, String> submitBtnRequiredAttrs = Map.of("type", "submit", "value", "Добавить");
        baseDTO.setSubmitBtnRequiredAttrs(submitBtnRequiredAttrs);
        baseDTO.setSubmitBtText("Добавить");
        baseDTO.setWaitType(HtmlWaiterProcessor.WaitType.CONTAINER_TEXT_LIST);
        baseDTO.setWaitLocators(List.of(".website-div"));
        dealTestValidStyle1(baseDTO, url, cookies);
    }


    @Test
    public void testStyle3ValidNewWebInfo() {
        String cookies = "JSESSIONID=D277431DA630D72DF7587F0BB8BC20AA";
        String url = "http://127.0.0.1:8080/web-info/new";
        baseDTO.setFormName("web_info_new_form");
        Map<String, String> submitBtnRequiredAttrs = Map.of("type", "submit", "value", "Добавить");
        baseDTO.setSubmitBtnRequiredAttrs(submitBtnRequiredAttrs);
        baseDTO.setSubmitBtText("Добавить");
        baseDTO.setWaitType(HtmlWaiterProcessor.WaitType.CONTAINER_TEXT_LIST);
        baseDTO.setWaitLocators(List.of(".website-div"));
        dealTestValidStyle2(baseDTO, url, cookies);
    }


    @Test
    public void testStyle1ValidEditWebInfo() {
        String cookies = "JSESSIONID=D277431DA630D72DF7587F0BB8BC20AA";
        String url = "http://127.0.0.1:8080/web-info/4";
        baseDTO.setFormName("web_info_edit_form");
        Map<String, String> submitBtnRequiredAttrs = Map.of("type", "submit", "value", "Изменить");
        baseDTO.setSubmitBtnRequiredAttrs(submitBtnRequiredAttrs);
        baseDTO.setSubmitBtText("Изменить");
        baseDTO.setWaitType(HtmlWaiterProcessor.WaitType.CONTAINER_TEXT_LIST);
        baseDTO.setWaitLocators(List.of(".website-div"));
        dealTestValidStyle0(baseDTO, url, cookies);
    }


    @Test
    public void testStyle2ValidEditWebInfo() {
        String cookies = "JSESSIONID=D277431DA630D72DF7587F0BB8BC20AA";
        String url = "http://127.0.0.1:8080/web-info/4";
        baseDTO.setFormName("web_info_edit_form");
        Map<String, String> submitBtnRequiredAttrs = Map.of("type", "submit", "value", "Изменить");
        baseDTO.setSubmitBtnRequiredAttrs(submitBtnRequiredAttrs);
        baseDTO.setSubmitBtText("Изменить");
        baseDTO.setWaitType(HtmlWaiterProcessor.WaitType.CONTAINER_TEXT_LIST);
        baseDTO.setWaitLocators(List.of(".website-div"));
        dealTestValidStyle1(baseDTO, url, cookies);
    }


    @Test
    public void testStyle3ValidEditWebInfo() {
        String cookies = "JSESSIONID=D277431DA630D72DF7587F0BB8BC20AA";
        String url = "http://127.0.0.1:8080/web-info/4";
        baseDTO.setFormName("web_info_edit_form");
        Map<String, String> submitBtnRequiredAttrs = Map.of("type", "submit", "value", "Изменить");
        baseDTO.setSubmitBtnRequiredAttrs(submitBtnRequiredAttrs);
        baseDTO.setSubmitBtText("Изменить");
        baseDTO.setWaitType(HtmlWaiterProcessor.WaitType.CONTAINER_TEXT_LIST);
        baseDTO.setWaitLocators(List.of(".website-div"));
        dealTestValidStyle2(baseDTO, url, cookies);
    }
}
