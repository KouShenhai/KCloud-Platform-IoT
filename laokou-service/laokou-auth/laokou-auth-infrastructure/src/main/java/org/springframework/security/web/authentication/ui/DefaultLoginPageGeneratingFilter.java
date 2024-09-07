/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

/*
 * Copyright 2002-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.security.web.authentication.ui;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import org.laokou.auth.api.TenantsServiceI;
import org.laokou.common.core.utils.SpringContextUtil;
import org.laokou.common.i18n.dto.Option;
import org.laokou.common.i18n.dto.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.util.CssUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * For internal use with namespace configuration in the case where a user doesn't
 * configure a login page. The configuration code will insert this filter in the chain
 * instead.
 * <p>
 * Will only work if a redirect is used to the login page.
 *
 * @author Luke Taylor
 * @author laokou
 * @since 2.0
 */
@Data
public class DefaultLoginPageGeneratingFilter extends GenericFilterBean {

	public static final String DEFAULT_LOGIN_PAGE_URL = "/login";

	public static final String ERROR_PARAMETER_NAME = "error";

	private String loginPageUrl;

	private String logoutSuccessUrl;

	private String failureUrl;

	private boolean formLoginEnabled;

	private boolean oauth2LoginEnabled;

	private boolean saml2LoginEnabled;

	private String authenticationUrl;

	private String usernameParameter;

	private String passwordParameter;

	private String rememberMeParameter;

	private Map<String, String> oauth2AuthenticationUrlToClientName;

	private Map<String, String> saml2AuthenticationUrlToProviderName;

	private Function<HttpServletRequest, Map<String, String>> resolveHiddenInputs = (request) -> Collections.emptyMap();

	public DefaultLoginPageGeneratingFilter() {
	}

	public DefaultLoginPageGeneratingFilter(UsernamePasswordAuthenticationFilter authFilter) {
		this.loginPageUrl = DEFAULT_LOGIN_PAGE_URL;
		this.logoutSuccessUrl = DEFAULT_LOGIN_PAGE_URL + "?logout";
		this.failureUrl = DEFAULT_LOGIN_PAGE_URL + "?" + ERROR_PARAMETER_NAME;
		if (authFilter != null) {
			initAuthFilter(authFilter);
		}
	}

	private void initAuthFilter(UsernamePasswordAuthenticationFilter authFilter) {
		this.formLoginEnabled = true;
		this.usernameParameter = authFilter.getUsernameParameter();
		this.passwordParameter = authFilter.getPasswordParameter();
		if (authFilter.getRememberMeServices() instanceof AbstractRememberMeServices rememberMeServices) {
			this.rememberMeParameter = rememberMeServices.getParameter();
		}
	}

	/**
	 * Sets a Function used to resolve a Map of the hidden inputs where the key is the
	 * name of the input and the value is the value of the input. Typically this is used
	 * to resolve the CSRF token.
	 * @param resolveHiddenInputs the function to resolve the inputs
	 */
	public void setResolveHiddenInputs(Function<HttpServletRequest, Map<String, String>> resolveHiddenInputs) {
		Assert.notNull(resolveHiddenInputs, "resolveHiddenInputs cannot be null");
		this.resolveHiddenInputs = resolveHiddenInputs;
	}

	public boolean isEnabled() {
		return this.formLoginEnabled || this.oauth2LoginEnabled || this.saml2LoginEnabled;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
	}

	private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		boolean loginError = isErrorPage(request);
		boolean logoutSuccess = isLogoutSuccess(request);
		if (isLoginUrlRequest(request) || loginError || logoutSuccess) {
			String loginPageHtml = generateLoginPageHtml(request, loginError, logoutSuccess);
			response.setContentType("text/html;charset=UTF-8");
			response.setContentLength(loginPageHtml.getBytes(StandardCharsets.UTF_8).length);
			response.getWriter().write(loginPageHtml);
			return;
		}
		chain.doFilter(request, response);
	}

	private String generateLoginPageHtml(HttpServletRequest request, boolean loginError, boolean logoutSuccess) {
		TenantsServiceI serviceI = SpringContextUtil.getBean(TenantsServiceI.class);
		Result<List<Option>> result = serviceI.listOption();
		String errorMsg = loginError ? getLoginErrorMessage(request) : "Invalid credentials";
		String contextPath = request.getContextPath();
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE html>\n");
		sb.append("<html lang=\"en\">\n");
		sb.append("  <head>\n");
		sb.append("    <meta charset=\"utf-8\">\n");
		sb.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n");
		sb.append("    <meta name=\"description\" content=\"\">\n");
		sb.append("    <meta name=\"author\" content=\"\">\n");
		sb.append("    <title>老寇IoT云平台统一认证</title>\n");
		sb.append(CssUtils.getCssStyleBlock().indent(4));
		sb.append("  </head>\n");
		sb.append("  <body>\n");
		sb.append("     <div class=\"content\">\n");
		if (this.formLoginEnabled) {
			sb.append("      <form class=\"login-form\" method=\"post\" action=\"")
				.append(contextPath)
				.append(this.authenticationUrl)
				.append("\">\n");
			sb.append("		   <div style='text-align:center;'>\n");
			sb.append("        	<h3>老寇IoT云平台统一认证</h3>\n");
			sb.append("		   </div>\n");
			sb.append(createError(loginError, errorMsg)).append(createLogoutSuccess(logoutSuccess));
			sb.append("        <p>\n");
			sb.append("          <label for=\"tenantId\" class=\"screenreader\">租户</label>\n");
			sb.append("			  <select name=\"tenant_id\" id=\"tenant_id\">\n");
			sb.append("           	<option value=\"")
				.append("0")
				.append("\" selected=\"selected\">\n")
				.append("老寇云集团")
				.append("</option>");
			for (Option option : result.getData()) {
				sb.append("           	<option value=\"")
					.append(option.getValue())
					.append("\">\n")
					.append(option.getLabel())
					.append("</option>");
			}
			sb.append("           </select>\n");
			sb.append("        </p>\n");
			sb.append("        <p>\n");
			sb.append("          <label for=\"username\" class=\"screenreader\">用户名</label>\n");
			sb.append("          <input type=\"text\" id=\"username\" name=\"")
				.append(this.usernameParameter)
				.append("\" placeholder=\"用户名\" required autofocus>\n");
			sb.append("        </p>\n");
			sb.append("        <p>\n");
			sb.append("          <label for=\"password\" class=\"screenreader\">密码</label>\n");
			sb.append("          <input type=\"password\" id=\"password\" name=\"")
				.append(this.passwordParameter)
				.append("\" placeholder=\"密码\" required>\n");
			sb.append("        </p>\n");
			sb.append(createRememberMe(this.rememberMeParameter)).append(renderHiddenInputs(request));
			sb.append("        <button type=\"submit\" class=\"primary\">登录</button>\n");
			sb.append("      </form>\n");
		}
		if (this.oauth2LoginEnabled) {
			sb.append("<h2>Login with OAuth 2.0</h2>");
			sb.append(createError(loginError, errorMsg));
			sb.append(createLogoutSuccess(logoutSuccess));
			sb.append("<table class=\"table table-striped\">\n");
			for (Map.Entry<String, String> clientAuthenticationUrlToClientName : this.oauth2AuthenticationUrlToClientName
				.entrySet()) {
				sb.append(" <tr><td>");
				String url = clientAuthenticationUrlToClientName.getKey();
				sb.append("<a href=\"").append(contextPath).append(url).append("\">");
				String clientName = HtmlUtils.htmlEscape(clientAuthenticationUrlToClientName.getValue());
				sb.append(clientName);
				sb.append("</a>");
				sb.append("</td></tr>\n");
			}
			sb.append("</table>\n");
		}
		if (this.saml2LoginEnabled) {
			sb.append("<h2>Login with SAML 2.0</h2>");
			sb.append(createError(loginError, errorMsg));
			sb.append(createLogoutSuccess(logoutSuccess));
			sb.append("<table class=\"table table-striped\">\n");
			for (Map.Entry<String, String> relyingPartyUrlToName : this.saml2AuthenticationUrlToProviderName
				.entrySet()) {
				sb.append(" <tr><td>");
				String url = relyingPartyUrlToName.getKey();
				sb.append("<a href=\"").append(contextPath).append(url).append("\">");
				String partyName = HtmlUtils.htmlEscape(relyingPartyUrlToName.getValue());
				sb.append(partyName);
				sb.append("</a>");
				sb.append("</td></tr>\n");
			}
			sb.append("</table>\n");
		}
		sb.append("</div>\n");
		sb.append("</body></html>");
		return sb.toString();
	}

	private String getLoginErrorMessage(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return "Invalid credentials";
		}
		if (!(session
			.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION) instanceof AuthenticationException exception)) {
			return "Invalid credentials";
		}
		if (!StringUtils.hasText(exception.getMessage())) {
			return "Invalid credentials";
		}
		return exception.getMessage();
	}

	private String renderHiddenInputs(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> input : this.resolveHiddenInputs.apply(request).entrySet()) {
			sb.append("<input name=\"");
			sb.append(input.getKey());
			sb.append("\" type=\"hidden\" value=\"");
			sb.append(input.getValue());
			sb.append("\" />\n");
		}
		return sb.toString();
	}

	private String createRememberMe(String paramName) {
		if (paramName == null) {
			return "";
		}
		return "<p><input type='checkbox' name='" + paramName + "'/> Remember me on this computer.</p>\n";
	}

	private boolean isLogoutSuccess(HttpServletRequest request) {
		return this.logoutSuccessUrl != null && matches(request, this.logoutSuccessUrl);
	}

	private boolean isLoginUrlRequest(HttpServletRequest request) {
		return matches(request, this.loginPageUrl);
	}

	private boolean isErrorPage(HttpServletRequest request) {
		return matches(request, this.failureUrl);
	}

	private String createError(boolean isError, String message) {
		if (!isError) {
			return "";
		}
		return "<div class=\"alert alert-danger\" role=\"alert\">" + HtmlUtils.htmlEscape(message) + "</div>";
	}

	private String createLogoutSuccess(boolean isLogoutSuccess) {
		if (!isLogoutSuccess) {
			return "";
		}
		return "<div class=\"alert alert-success\" role=\"alert\">You have been signed out</div>";
	}

	private boolean matches(HttpServletRequest request, String url) {
		if (!"GET".equals(request.getMethod()) || url == null) {
			return false;
		}
		String uri = request.getRequestURI();
		int pathParamIndex = uri.indexOf(';');
		if (pathParamIndex > 0) {
			// strip everything after the first semi-colon
			uri = uri.substring(0, pathParamIndex);
		}
		if (request.getQueryString() != null) {
			uri += "?" + request.getQueryString();
		}
		if ("".equals(request.getContextPath())) {
			return uri.equals(url);
		}
		return uri.equals(request.getContextPath() + url);
	}

}
