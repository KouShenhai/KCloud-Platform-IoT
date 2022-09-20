package io.laokou.gateway.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.laokou.gateway.component.Resilience4jFallbackHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

import java.time.Duration;

@Configuration
public class RouterFunctionConfig {

    @Autowired
    private Resilience4jFallbackHandler resilience4jFallbackHandler;

    @Bean
    public RouterFunction routerFunction() {
        return RouterFunctions.route(RequestPredicates.path("/fallback").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), resilience4jFallbackHandler);
    }

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                // circuitBreaker 使用默认配置
                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .build());
    }

}
