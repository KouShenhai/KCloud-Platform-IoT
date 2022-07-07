package io.laokou.gateway.config;

import io.laokou.gateway.exception.GatewayExceptionHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;
import java.util.Collections;
import java.util.List;
/**
 * 自定义异常处理
 * @author Kou Shenhai
 */
@Configuration
public class ErrorWebFluxAutoConfig {

    @Primary
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public GatewayExceptionHandler errorWebExceptionHandler(ObjectProvider<List<ViewResolver>> provider,
                                                            ServerCodecConfigurer serverCodecConfigurer) {
        GatewayExceptionHandler handler = new GatewayExceptionHandler();
        handler.setViewResolvers(provider.getIfAvailable(Collections::emptyList));
        handler.setMessageWriters(serverCodecConfigurer.getWriters());
        handler.setMessageReaders(serverCodecConfigurer.getReaders());
        return handler;
    }

}
