package mo.must.chat.common.exception;


public class BizException extends Exception {
    protected ErrorMsg errorMsg;

    public BizException() {
        this.errorMsg = new ErrorMsg();
    }

    public BizException(String msgCode) {
        this.errorMsg = new ErrorMsg(msgCode);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
        this.errorMsg = new ErrorMsg();
    }

    public BizException(String msgCode, String[] msgParams, String message, Throwable cause) {
        super(message, cause);
        this.errorMsg = new ErrorMsg(msgCode, msgParams);
    }

    public BizException(String msgCode, String[] msgParams, String message) {
        super(message);
        this.errorMsg = new ErrorMsg(msgCode, msgParams);
    }

    public BizException(String msgCode, String message, Throwable cause) {
        super(message, cause);
        this.errorMsg = new ErrorMsg(msgCode);
    }

    public BizException(String msgCode, String message) {
        super(message);
        this.errorMsg = new ErrorMsg(msgCode);
    }

    public BizException(String msgCode, String msgParam, String message, Throwable cause) {
        super(message, cause);
        this.errorMsg = new ErrorMsg(msgCode, msgParam);
    }

    public ErrorMsg getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(ErrorMsg errorMsg) {
        this.errorMsg = errorMsg;
    }
}

