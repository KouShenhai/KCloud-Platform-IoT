package io.laokou.admin.infrastructure.config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
/**
 * @author Kou Shenhai
 */
@Component
@Slf4j
public class WebsocketSubscribeListener implements ApplicationListener<SessionSubscribeEvent> {

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
        log.info("subscribe...");
    }
}
