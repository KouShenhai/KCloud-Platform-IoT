package io.laokou.log.event;

import org.springframework.context.ApplicationEvent;

/**
 * 登录日志事件
 */
public class LoginLogEvent extends ApplicationEvent {
    public LoginLogEvent(Object source) {
        super(source);
    }
}
