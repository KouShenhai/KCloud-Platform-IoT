package io.laokou.admin.infrastructure.config;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.util.WebAppRootListener;
import javax.servlet.ServletContext;
/**
 * @author  Kou Shenhai
 */
@Configuration
public class WebSocketConfig implements ServletContextInitializer {
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }

    @Override
    public void onStartup(ServletContext servletContext) {
        servletContext.addListener(WebAppRootListener.class);
        servletContext.setInitParameter("org.apache.tomcat.websocket.textBufferSize","52428800");
        servletContext.setInitParameter("org.apache.tomcat.websocket.binaryBufferSize","52428800");
    }
}
