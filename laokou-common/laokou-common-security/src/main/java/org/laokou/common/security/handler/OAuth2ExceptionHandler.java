package org.laokou.common.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.laokou.common.core.utils.ResponseUtil;
import org.laokou.common.i18n.common.StatusCodes;
import org.laokou.common.i18n.utils.MessageUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * @author laokou
 */
public class OAuth2ExceptionHandler {

	public static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

	public static OAuth2AuthenticationException getException(String errorCode, String description, String uri) {
		return new OAuth2AuthenticationException(new OAuth2Error(errorCode, description, uri));
	}

	public static OAuth2AuthenticationException getException(int errorCode, String description) {
		return getException(String.valueOf(errorCode), description, ERROR_URI);
	}

	@SneakyThrows
	public static void handleAccessDenied(HttpServletRequest request, HttpServletResponse response, Throwable ex) {
		if (ex instanceof AccessDeniedException) {
			ResponseUtil.response(response, StatusCodes.FORBIDDEN, MessageUtil.getMessage(StatusCodes.FORBIDDEN));
		}
	}

	@SneakyThrows
	public static void handleAuthentication(HttpServletRequest request, HttpServletResponse response, Throwable ex) {
		if (ex instanceof OAuth2AuthenticationException authenticationException) {
			String message = authenticationException.getError().getDescription();
			int errorCode = Integer.parseInt(authenticationException.getError().getErrorCode());
			ResponseUtil.response(response, errorCode, message);
			return;
		}
		if (ex instanceof InsufficientAuthenticationException) {
			ResponseUtil.response(response, StatusCodes.UNAUTHORIZED, MessageUtil.getMessage(StatusCodes.UNAUTHORIZED));
		}
	}

}
