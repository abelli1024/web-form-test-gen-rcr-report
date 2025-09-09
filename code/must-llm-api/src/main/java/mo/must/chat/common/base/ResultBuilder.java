package mo.must.chat.common.base;

import mo.must.chat.common.exception.BizException;
import mo.must.chat.common.util.MessageUtil;
import org.apache.commons.lang3.StringUtils;


public class ResultBuilder {
    public ResultBuilder() {
    }

    public static <T> ResultDTO<T> success(T data) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setSuccess(true);
        resultDTO.setData(data);
        resultDTO.setCode(null);
        resultDTO.setMsg(null);
        return resultDTO;
    }


    public static <T> ResultDTO<T> failure(String errCode, String errMsg) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setSuccess(false);
        resultDTO.setCode(errCode);
        resultDTO.setMsg(errMsg);
        return resultDTO;
    }

    public static <T> ResultDTO<T> failure(String errCode) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setSuccess(false);
        String errMsg = MessageUtil.getMessage(errCode);
        resultDTO.setCode(errCode);
        resultDTO.setMsg(errMsg);
        return resultDTO;
    }

    public static <T> ResultDTO<T> failure(Throwable t) {
        String _errCode = null;
        String _errMsg = null;
        if (t instanceof BizException) {
            BizException e = (BizException) t;
            _errCode = e.getErrorMsg().getCode();
            _errMsg = e.getMessage();
            if (_errMsg == null || _errMsg.trim().length() == 0) {
                _errMsg = e.getMessage();
            }
        } else {
            _errCode = "-9";
            _errMsg = "Service exception";
        }
        if (StringUtils.isBlank(_errMsg)){
            _errMsg = MessageUtil.getMessage(_errCode);
        }
        return failure(_errCode, _errMsg);
    }
}
