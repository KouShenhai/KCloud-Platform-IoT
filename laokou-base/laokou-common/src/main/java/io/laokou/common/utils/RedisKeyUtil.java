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
     * 模板参数Key
     */
    public final static String getTemplateParamsKey() {
        return "sys:template:params";
    }

    /**
     * 防止刷验证码Key
     */
    public final static String checkUserCaptchaKey(String uuid) {
        return "sys:user:captcha:check:" + uuid;
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

    /**
     * 微信认证Key
     */
    public final static String getWxTokenKey() {
        return "sys:wx:token";
    }

    /**
     * 资源信息Key
     */
    public final static String getResourceInfoKey(String id){
        return "sys:res:info:" + id;
    }

    /**
     * 模板信息Key
     */
    public final static String getTemplateInfoKey(String identify){
        return "sys:template:info:" + identify;
    }

    /**
     * 在线用户
     */
    public final static String getOnlineUserKey() {
        return "sys:user:online";
    }

    /**
     * 订单信息Key
     */
    public final static String getOrderInfoKey(Long orderId) {
        return "sys:order:info:" + orderId;
    }

    /**
     * 订单信息Key
     */
    public final static String getWxOpenidKey(String eventKey) {
        return "sys:wx:openid:" + eventKey;
    }

    /**
     * 同义词信息Key
     * @return
     */
    public final static String getEsSynonymKey() {
        return "sys:es:synonym";
    }

    /**
     * 日志信息Key
     * @return
     */
    public final static String getLogInfoKey() {
        return "sys:log:info";
    }
}
