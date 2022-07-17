package io.laokou.common.utils;
/**
 * @author  Kou Shenhai
 * @since 1.0.0
 */
public final class RedisKeyUtil {

    /**
     * 验证码Key
     */
    public final static String getUserCaptchaKey(String uuid) {
        return "sys:user:captcha:" + uuid;
    }

    /**
     * 用户资源Key
     */
    public final static String getUserResourceKey(Long userId) {
        return "sys:user:resource:" + userId;
    }

    /**
     * 用户信息Key
     */
    public final static String getUserInfoKey(Long userId) {
        return "sys:user:info:" + userId;
    }
}
