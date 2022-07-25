package io.laokou.common.constant;

/**
 * 常量
 * @author Kou Shenhai
 */
public interface Constant {

    String UTF8 = "UTF-8";

    /**
     * Bearer
     */
    String BEARER = "Bearer ";

    String ACCESS_TOKEN = "access_token";

    String URI = "uri";

    String METHOD = "method";

    String UUID = "uuid";
    /**
     * Authorization header
     */
    String AUTHORIZATION_HEADER = "Authorization";
    /**
     * basic
     */
    String BASIC = "Basic ";
    /**
     * 成功
     */
    Integer SUCCESS = 1;
    /**
     * 失败
     */
    Integer FAIL = 0;
    /**
     * header
     */
    String HEADER = "header";
    /**
     * 用户标识
     */
    String USER_KEY_HEAD = "userId";
    /**
     * 用户名
     */
    String USERNAME_HEAD = "username";
    /**
     * no
     */
    Integer NO = 0;
    /**
     * yes
     */
    Integer YES = 0;
    /**
     * 认证标识
     */
    String TICKET = "ticket";
}
