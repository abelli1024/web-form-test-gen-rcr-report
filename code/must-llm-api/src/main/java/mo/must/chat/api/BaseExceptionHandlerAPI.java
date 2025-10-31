package mo.must.chat.api;

import mo.must.chat.common.base.ResultBuilder;
import mo.must.chat.common.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class BaseExceptionHandlerAPI {

    @ExceptionHandler(value = {BizException.class})
    @ResponseBody
    public Object errorBizException(BizException e) {
        log.error(e.getMessage(), e);
        String code = e.getErrorMsg().getCode();
        if (code.equals("900002")) {
            return ResultBuilder.failure("900002");
        }
        return ResultBuilder.failure(e);
    }


    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public Object errorException(Exception e) {
        log.error(e.getMessage(), e);
        return ResultBuilder.failure("900001");
    }


    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    @ResponseBody
    public Object errorHttpMessageNotReadableException(Exception e) {
        log.error(e.getMessage(), e);
        return ResultBuilder.failure("900003");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {NoHandlerFoundException.class})
    @ResponseBody
    public Object noHandlerFoundException(HttpServletRequest request, NoHandlerFoundException e) throws BizException {
        return ResultBuilder.failure("900013");
    }
}
