package io.laokou.admin.infrastructure.config;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
/**
 * @author Kou Shenhai
 */
@Component
public class WebsocketSubscribeListener implements ApplicationListener<SessionSubscribeEvent> {

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {

    }
}
