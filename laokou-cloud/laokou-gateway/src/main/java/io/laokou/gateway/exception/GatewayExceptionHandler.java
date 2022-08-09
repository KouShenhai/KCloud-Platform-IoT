package io.laokou.gateway.exception;
import com.alibaba.ttl.TransmittableThreadLocal;
import feign.FeignException;
import io.laokou.common.exception.ErrorCode;
import io.laokou.common.utils.HttpResultUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.Collections;
import java.util.List;
/**
 * 异常处理器
 * @author Kou Shenhai
 * @since 1.0.0
 */
@Setter
@Getter
@Slf4j
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

	private List<HttpMessageReader<?>> messageReaders = Collections.emptyList();
	private List<HttpMessageWriter<?>> messageWriters = Collections.emptyList();
	private List<ViewResolver> viewResolvers = Collections.emptyList();
	private static final TransmittableThreadLocal<HttpResultUtil<?>> threadLocal = new TransmittableThreadLocal<>();

	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable e) {
		log.error("网关全局处理异常，异常信息:{}",e.getMessage());
		HttpResultUtil<Boolean> result = new HttpResultUtil<>();
		if (e instanceof FeignException) {
			log.error("远程调用失败：{}",e.getMessage());
			result = parseFeignException(e);
		} else if (e instanceof NotFoundException || e instanceof RuntimeException){
			log.error("服务未启动或服务运行异常");
			result = result.error(ErrorCode.SERVICE_MAINTENANCE);
		}
		threadLocal.set(result);
		ServerRequest serverRequest = ServerRequest.create(exchange, this.messageReaders);
		return RouterFunctions.route(RequestPredicates.all(),this::renderErrorResponse)
				.route(serverRequest)
				.switchIfEmpty(Mono.error(e))
				.flatMap((handler) -> handler.handle(serverRequest))
				.flatMap((response) -> write(exchange,response));
	}

	protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
		return ServerResponse.status(threadLocal.get().code)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(BodyInserters.fromObject(threadLocal.get()));
	}

	private Mono<? extends Void> write(ServerWebExchange exchange,ServerResponse response) {
		final HttpHeaders exchangeHeaders = exchange.getResponse().getHeaders();
		final MediaType responseContentType = response.headers().getContentType();
		log.info("exchangeHeaders:{}",exchangeHeaders);
		log.info("responseContentType:{}",responseContentType);
		exchangeHeaders.setContentType(responseContentType);
		threadLocal.remove();
		return response.writeTo(exchange,new ResponseContext());
	}

	private class ResponseContext implements ServerResponse.Context {

		@Override
		public List<HttpMessageWriter<?>> messageWriters() {
			return GatewayExceptionHandler.this.messageWriters;
		}

		@Override
		public List<ViewResolver> viewResolvers() {
			return GatewayExceptionHandler.this.viewResolvers;
		}
	}

	private HttpResultUtil<Boolean> parseFeignException(Throwable e) {
		FeignException fx = (FeignException) e;
		HttpResultUtil<Boolean> result = new HttpResultUtil<>();
		int status = fx.status();
		switch (status) {
			default: result.error("服务调用失败，请联系管理员");
		}
		return result;
	}

}
