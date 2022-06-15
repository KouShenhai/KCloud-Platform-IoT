package io.laokou.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2020/9/4 0004 下午 8:01
 */
@Configuration
@Slf4j
public class CorsConfig{

    private final static String MAX_AGE = "18000L";

    @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) -> {
            //获取request对象
            ServerHttpRequest request = serverWebExchange.getRequest();
            if (!CorsUtils.isCorsRequest(request)) {
                return webFilterChain.filter(serverWebExchange);
            }
            //获取请求头
            HttpHeaders requestHeaders = request.getHeaders();
            //获取response对象
            ServerHttpResponse response = serverWebExchange.getResponse();
            //获取请求头的请求方法
            HttpMethod requestMethod = requestHeaders.getAccessControlRequestMethod();
            //获取响应头
            HttpHeaders responseHeaders = response.getHeaders();
            responseHeaders.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, requestHeaders.getOrigin());
            responseHeaders.addAll(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, requestHeaders.getAccessControlRequestHeaders());
            if (requestMethod != null) {
                responseHeaders.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, requestMethod.name());
            }
            responseHeaders.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
            responseHeaders.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, CorsConfiguration.ALL);
            responseHeaders.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, MAX_AGE);
            //获取方法
            HttpMethod method = request.getMethod();
            if (method == HttpMethod.OPTIONS) {
                response.setStatusCode(HttpStatus.OK);
                log.info("header：{}",request.getHeaders());
                return Mono.empty();
            }
            return webFilterChain.filter(serverWebExchange);
        };
    }

}
