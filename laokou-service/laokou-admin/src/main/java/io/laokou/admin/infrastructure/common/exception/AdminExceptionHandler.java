package io.laokou.admin.infrastructure.common.exception;
import io.laokou.common.exception.ErrorCode;
import io.laokou.common.utils.HttpResultUtil;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/**
 * admin异常处理器
 * @author Kou Shenhai
 * @since 1.0.0
 */
@RestControllerAdvice
@ResponseBody
@Component
public class AdminExceptionHandler {

    /**
     * 处理自定义异常
     */
    @ExceptionHandler({IncorrectCredentialsException.class})
    public HttpResultUtil<Boolean> handleRenException(){
        return new HttpResultUtil<Boolean>().error(ErrorCode.AUTHORIZATION_INVALID);
    }

}
