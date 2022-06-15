package io.laokou.common.exception;

import io.laokou.common.utils.MessageUtil;
import lombok.Data;

/**
 * 自定义异常
 * @author Kou Shenhai
 */
@Data
public class CustomException extends RuntimeException{

    private int code;
    private String msg;

    public CustomException(int code) {
        this.code = code;
        this.msg = MessageUtil.getMessage(code);
    }

    public CustomException(int code, String... params) {
        this.code = code;
        this.msg = MessageUtil.getMessage(code, params);
    }

    public CustomException(int code, Throwable e) {
        super(e);
        this.code = code;
        this.msg = MessageUtil.getMessage(code);
    }

    public CustomException(int code, Throwable e, String... params) {
        super(e);
        this.code = code;
        this.msg = MessageUtil.getMessage(code, params);
    }

    public CustomException(String msg) {
        super(msg);
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = msg;
    }

    public CustomException(String msg, Throwable e) {
        super(msg, e);
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = msg;
    }

}
