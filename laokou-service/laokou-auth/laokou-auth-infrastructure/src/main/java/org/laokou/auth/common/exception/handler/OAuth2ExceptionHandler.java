package org.laokou.auth.common.exception.handler;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * @author laokou
 */
public class OAuth2ExceptionHandler {

	public static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

	public static OAuth2AuthenticationException getException(String errorCode, String description, String uri) {
		OAuth2Error error = new OAuth2Error(errorCode, description, uri);
		return new OAuth2AuthenticationException(error);
	}

	public static OAuth2AuthenticationException getException(int errorCode, String description) {
		return getException(String.valueOf(errorCode), description, ERROR_URI);
	}

	public static OAuth2AuthenticationException getException(String errorCode, String description) {
		return getException(errorCode, description, ERROR_URI);
	}

}
