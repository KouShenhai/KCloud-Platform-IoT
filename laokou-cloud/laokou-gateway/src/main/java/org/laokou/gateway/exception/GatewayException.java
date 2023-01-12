package org.laokou.gateway.exception;

import lombok.Getter;

/**
 * @author laokou
 */
public enum GatewayException {

    /**
     * 服务正在维护，请联系管理员
     */
    SERVICE_MAINTENANCE(500,"服务正在维护，请联系管理员"),
    /**
     * 未授权
     */
    UNAUTHORIZED(401,"未授权"),
    /**
     * 未知错误
     */
    UNKNOWN_ERROR(505,"未知错误");

    @Getter
    private final int code;
    @Getter
    private final String msg;

    GatewayException(int code,String msg) {
        this.code = code;
        this.msg = msg;
    }

}
