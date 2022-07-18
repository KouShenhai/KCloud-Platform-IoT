package io.laokou.admin.infrastructure.config;
import io.laokou.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import java.util.*;
@Component
public class WebsocketDisconnectListener implements ApplicationListener<SessionDisconnectEvent> {

    @Autowired
    private RedisUtil redisUtil;

    private ThreadLocal<Map<String, Object>> local = new ThreadLocal<>();

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {

    }
}
