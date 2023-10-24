package org.laokou.common.security.handler;

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
		try (PrintWriter writer = response.getWriter()) {
			response.setStatus(HttpStatus.OK.value());
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
			writer.write(JacksonUtil.toJsonStr(Result.fail(code, message)));
			writer.flush();
		}
	}

	public static OAuth2AuthenticationException getException(String errorCode, String description, String uri) {
		return new OAuth2AuthenticationException(new OAuth2Error(errorCode, description, uri));
	}

	public static OAuth2AuthenticationException getException(int errorCode, String description) {
		return getException(String.valueOf(errorCode), description, ERROR_URI);
	}

}
