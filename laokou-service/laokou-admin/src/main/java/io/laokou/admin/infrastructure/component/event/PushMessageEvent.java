package io.laokou.admin.infrastructure.component.event;

import org.springframework.context.ApplicationEvent;

public class PushMessageEvent extends ApplicationEvent {
    public PushMessageEvent(Object source) {
        super(source);
    }
}
