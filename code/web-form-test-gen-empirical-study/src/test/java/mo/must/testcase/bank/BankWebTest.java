package mo.must.testcase.bank;

import mo.must.base.BaseDTO;
import mo.must.base.BaseTest;
import mo.must.processor.HtmlWaiterProcessor;
import org.junit.Before;
import org.junit.Test;

import java.util.List;


public class BankWebTest extends BaseTest {

    private BaseDTO baseDTO;

    @Before
    public void init() {
        baseDTO = new BaseDTO();
        initBasicBaseDTO(baseDTO, "bank_web");
    }

    @Test
    public void testStyle1ValidLogin() {
        String url = "http://127.0.0.1:8080/login";
        baseDTO.setFormName("login_form");
        baseDTO.setSubmitBtText("Login");
        baseDTO.setWaitType(HtmlWaiterProcessor.WaitType.CONTAINER_TEXT_LIST);
        baseDTO.setWaitLocators(List.of(".alert.alert-success", ".alert.alert-danger"));
        dealTestValidStyle0(baseDTO, url);
    }

    @Test
    public void testStyle2ValidLogin() {
        String url = "http://127.0.0.1:8080/login";
        baseDTO.setFormName("login_form");
        baseDTO.setSubmitBtText("Login");
        baseDTO.setWaitType(HtmlWaiterProcessor.WaitType.CONTAINER_TEXT_LIST);
        baseDTO.setWaitLocators(List.of(".alert.alert-success", ".alert.alert-danger"));
        dealTestValidStyle1(baseDTO, url);
    }


    @Test
    public void testStyle3ValidLogin() {
        String url = "http://127.0.0.1:8080/login";
        baseDTO.setFormName("login_form");
        baseDTO.setSubmitBtText("Login");
        baseDTO.setWaitType(HtmlWaiterProcessor.WaitType.CONTAINER_TEXT_LIST);
        baseDTO.setWaitLocators(List.of(".alert.alert-success", ".alert.alert-danger"));
        dealTestValidStyle2(baseDTO, url);
    }


    @Test
    public void testStyle1ValidRegister() {
        String url = "http://127.0.0.1:8080/register";
        baseDTO.setFormName("register_form");
        baseDTO.setSubmitBtText("Register");
        baseDTO.setWaitType(HtmlWaiterProcessor.WaitType.CONTAINER_TEXT_LIST);
        baseDTO.setWaitLocators(List.of(".alert.alert-success", ".alert.alert-danger"));
        dealTestValidStyle0(baseDTO, url);
    }


    @Test
    public void testStyle2ValidRegister() {
        String url = "http://127.0.0.1:8080/register";
        baseDTO.setFormName("register_form");
        baseDTO.setSubmitBtText("Register");
        baseDTO.setWaitType(HtmlWaiterProcessor.WaitType.CONTAINER_TEXT_LIST);
        baseDTO.setWaitLocators(List.of(".alert.alert-success", ".alert.alert-danger"));
        dealTestValidStyle1(baseDTO, url);
    }


    @Test
    public void testStyle3ValidRegister() {
        String url = "http://127.0.0.1:8080/register";
        baseDTO.setFormName("register_form");
        baseDTO.setSubmitBtText("Register");
        baseDTO.setWaitType(HtmlWaiterProcessor.WaitType.CONTAINER_TEXT_LIST);
        baseDTO.setWaitLocators(List.of(".alert.alert-success", ".alert.alert-danger"));
        dealTestValidStyle2(baseDTO, url);
    }
}
