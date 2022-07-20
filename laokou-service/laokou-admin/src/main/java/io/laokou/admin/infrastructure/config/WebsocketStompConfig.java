package io.laokou.admin.infrastructure.config;
import io.laokou.common.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.Objects;
/**
 * 启动websocket
 * @author  Kou Shenhai
 *
 */
@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebsocketStompConfig implements WebSocketMessageBrokerConfigurer {
    /**
     * 心跳时间
     */
    private final Long HEART_BEAT = 15000L;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //符合这个请求路径
        registry.addEndpoint("/ws")
                .addInterceptors(new HandshakeInterceptor() {
                    /*
                     * websocket握手之前
                     */
                    @Override
                    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws IOException {
                        ServletServerHttpRequest request = (ServletServerHttpRequest) serverHttpRequest;
                        String userId = request.getServletRequest().getParameter(Constant.USER_KEY_HEAD);
                        if (StringUtils.isBlank(userId)) {
                            return false;
                        }
                        map.put(Constant.USER_KEY_HEAD, userId);
                        return true;
                    }
                    /**
                     * websocket握手之后
                     * @param serverHttpRequest
                     * @param serverHttpResponse
                     * @param webSocketHandler
                     * @param e
                     */
                    @Override
                    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {}
                })
                //握手之后
                .setHandshakeHandler(new DefaultHandshakeHandler() {
                    @Override
                    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                        String userId = attributes.get(Constant.USER_KEY_HEAD).toString();
                        log.info("userId：{}", userId);
                        Principals principals = new Principals(userId);
                        log.info("principal :{}", principals.getName());
                        return principals;
                    }
                })//设置允许跨域
                .setAllowedOrigins(CorsConfiguration.ALL)
                //在不支持websocket的浏览器，使用sockJs备选作为连接
                .withSockJS();
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //注意：setHeartbeatValue不能单独设置，不然不起作用，要配合后面setTaskScheduler才可以生效
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(1);
        taskScheduler.setThreadNamePrefix("websocket-thread-");
        taskScheduler.initialize();
        //定义一个（或多个）客户端地址的前缀信息，也就是客户端接收服务端发送消息的前缀信息
        registry.enableSimpleBroker("/one-to-one","/one-to-many", "/user")
                .setHeartbeatValue(new long[]{HEART_BEAT, HEART_BEAT})
                .setTaskScheduler(taskScheduler);
        //websocket请求前缀
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }
    /**
     * 消息传输参数配置
     * @param registry
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        //设置消息字节大小
        registry.setMessageSizeLimit(1 * 1024 * 1024 * 1024);
        //设置消息缓存大小
        registry.setSendBufferSizeLimit(1 * 1024 * 1024 * 1024);
        //设置超时
        registry.setSendTimeLimit(1 * 60 * 5 * 1000);
    }
    class Principals implements Principal {
        private String userId;
        public Principals(String userId) {
            this.userId = userId;
        }
        @Override
        public String getName() {
            return userId;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Principals)) {
                return false;
            }
            Principals that = (Principals) o;
            return Objects.equals(userId, that.userId);
        }
        @Override
        public int hashCode() {
            return userId != null ? userId.hashCode() : 0;
        }
    }
}
