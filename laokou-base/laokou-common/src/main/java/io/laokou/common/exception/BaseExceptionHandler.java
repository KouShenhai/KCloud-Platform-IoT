package io.laokou.common.exception;
import io.laokou.common.utils.HttpResultUtil;
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
public class BaseExceptionHandler {
	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler({CustomException.class})
	public HttpResultUtil<Boolean> handleRenException(CustomException ex){
		return new HttpResultUtil<Boolean>().error(ex.getCode(),ex.getMsg());
	}

}
