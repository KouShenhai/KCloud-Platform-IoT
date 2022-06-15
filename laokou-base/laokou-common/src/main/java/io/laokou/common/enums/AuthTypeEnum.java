package io.laokou.common.enums;

/**
 * @author  Kou Shenhai
 */

public enum AuthTypeEnum {
    /**
     * 权限认证
     */
    PERMISSIONS_AUTH(0),
    /**
     * 登录认证
     */
    LOGIN_AUTH(1),
    /**
     * 无需认证
     */
    NO_AUTH(2);


    private final Integer value;

    AuthTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer value() {
        return this.value;
    }
}
