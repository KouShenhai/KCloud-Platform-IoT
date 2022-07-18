package io.laokou.gateway.component;
import cn.hutool.http.HttpStatus;
import io.laokou.common.utils.HttpResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.util.Optional;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;
@Component
@Slf4j
public class HystrixFallbackHandler implements HandlerFunction<ServerResponse> {
    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        Optional<Object> optionalUris = request.attribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        optionalUris.ifPresent((uri) -> log.error("网关执行请求：{}失败，hystrix服务降级处理",uri));
        return ServerResponse.status(HttpStatus.HTTP_OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(new HttpResultUtil<Boolean>().error("服务已被降级熔断")));
    }
}
