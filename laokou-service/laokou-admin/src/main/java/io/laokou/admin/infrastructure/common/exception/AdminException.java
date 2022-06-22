package io.laokou.admin.infrastructure.common.exception;
import io.laokou.common.exception.ErrorCode;
import io.laokou.common.utils.HttpResultUtil;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/**
 * base异常处理器
 * @author Kou Shenhai
 * @since 1.0.0
 */
@RestControllerAdvice
@ResponseBody
@Component
public class AdminException {

    /**
     * 处理自定义异常
     */
    @ExceptionHandler({IncorrectCredentialsException.class})
    public HttpResultUtil<Boolean> handleRenException(IncorrectCredentialsException ex){
        return new HttpResultUtil<Boolean>().error(ErrorCode.AUTHORIZATION_INVALID);
    }

}
