package org.laokou.auth.common.exception.handler;

import jakarta.servlet.http.HttpServletResponse;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.i18n.dto.Result;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
public class OAuth2ExceptionHandler {

	public static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

	public static void response(HttpServletResponse response, int code, String message) throws IOException {
		response.setStatus(HttpStatus.OK.value());
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
		PrintWriter writer = response.getWriter();
		writer.write(JacksonUtil.toJsonStr(Result.fail(code, message)));
		writer.flush();
		writer.close();
	}

	public static OAuth2AuthenticationException getException(String errorCode, String description, String uri) {
		OAuth2Error error = new OAuth2Error(errorCode, description, uri);
		return new OAuth2AuthenticationException(error);
	}

	public static OAuth2AuthenticationException getException(int errorCode, String description) {
		return getException(String.valueOf(errorCode), description, ERROR_URI);
	}

}
