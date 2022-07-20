package io.laokou.admin.infrastructure.component.event;

import org.springframework.context.ApplicationEvent;

public class SaveMessageEvent extends ApplicationEvent {
    public SaveMessageEvent(Object source) {
        super(source);
    }
}
