package org.laokou.gateway.constant;

/**
 * @author laokou
 */
public interface GatewayConstant {
    /**
     * 请求链-用户id
     */
    String REQUEST_USER_ID = "userId";
    /**
     * 请求链-用户名
     */
    String REQUEST_USERNAME = "username";
    /**
     * 密码模式-请求地址
     */
    String OAUTH2_AUTH_URI = "/**/oauth2/token";

    /**
     * 密码模式-用户名
     */
    String USERNAME = "username";

    String ERROR_DESCRIPTION = "error_description";

    String ERROR = "error";

    /**
     * 密码模式-密码
     */
    String PASSWORD = "password";

    /**
     * xss 请求头
     */
    String XSS_TICKET_HEADER = "xss-ticket";
    /**
     * xss 白名单
     */
    String XSS_TICKET_VALUE = "xss-white";

    int SUCCESS = 200;
}
