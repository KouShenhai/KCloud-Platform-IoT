package io.laokou.admin.infrastructure.config;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
@Component
public class WebsocketConnectedListener implements ApplicationListener<SessionConnectedEvent> {

    @Override
    public void onApplicationEvent(SessionConnectedEvent sessionConnectedEvent) {

    }

}
