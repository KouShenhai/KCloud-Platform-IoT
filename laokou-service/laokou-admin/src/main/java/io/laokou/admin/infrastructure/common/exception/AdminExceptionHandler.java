package io.laokou.admin.infrastructure.common.exception;
import io.laokou.common.exception.ErrorCode;
import io.laokou.common.utils.HttpResultUtil;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/**
 * 异常处理器
 * @author Kou Shenhai
 * @since 1.0.0
 */
@RestControllerAdvice
@ResponseBody
@Component
public class AdminExceptionHandler {
	/**
	 * 授权失败
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(value = UnauthorizedException.class)
	public HttpResultUtil<Boolean> unauthorizedExceptionHandler(UnauthorizedException ex) {
		return new HttpResultUtil<Boolean>().error(ErrorCode.FORBIDDEN);
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public HttpResultUtil<Boolean> duplicateKeyExceptionHandler(DuplicateKeyException ex){
		return new HttpResultUtil<Boolean>().error(ErrorCode.DB_RECORD_EXISTS);
	}

	@ExceptionHandler(IncorrectCredentialsException.class)
	public HttpResultUtil<Boolean> incorrectCredentialsExceptionHandler(IncorrectCredentialsException ex){
		return new HttpResultUtil<Boolean>().error(ErrorCode.AUTHORIZATION_INVALID);
	}

}
