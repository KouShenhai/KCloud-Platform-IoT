package org.laokou.auth.common;

/**
 * 编码由5位数字构成 前2位为应用编号 后3位为业务编号
 */
public interface BizCode {

    /**
     * 唯一标识不能为空
     */
    int IDENTIFIER_NOT_NULL = 2001;

    /**
     * 验证码不能为空
     */
    int CAPTCHA_NOT_NULL = 2002;

    /**
     * 账号不能为空
     */
    int USERNAME_NOT_NULL = 2005;

    /**
     * 密码不能为空
     */
    int PASSWORD_NOT_NULL = 2009;

    /**
     * 手机号不能为空
     */
    int MOBILE_NOT_NULL = 2010;

    /**
     * 邮箱不能为空
     */
    int MAIL_NOT_NULL = 2012;

    /**
     * 租户编号不能为空，请选择租户
     */
    int TENANT_ID_NOT_NULL = 2014;

}
