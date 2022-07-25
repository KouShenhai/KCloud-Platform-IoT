package io.laokou.gateway.route;
import com.alibaba.fastjson.JSON;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/25 0025 下午 3:55
 */
@Slf4j
@Configuration
public class ApolloRouteDefinitionRepository implements RouteDefinitionRepository, ApplicationEventPublisherAware {

    private static final String ROUTES = "gateway.routes";

    private ApplicationEventPublisher publisher;

    @ApolloConfig
    private Config config;

    @ApolloConfigChangeListener(value = "application")
    private void changeHandler(ConfigChangeEvent event) {
        log.info("apollo动态拉取配置...");
        if (event.isChanged(ROUTES)) {
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
        }
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        final String property = config.getProperty(ROUTES, null);
        if (StringUtils.isBlank(property)) {
            Flux.fromIterable(Lists.newArrayList());
        }
        final List<RouteDefinition> routeDefinitions = JSON.parseArray(property, RouteDefinition.class);
        return Flux.fromIterable(routeDefinitions);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
}
