package io.laokou.log.feign.admin.factory;
import feign.hystrix.FallbackFactory;
import io.laokou.log.feign.admin.fallback.LogApiFeignClientFallback;
import org.springframework.stereotype.Component;
/**
 * 回调工厂
 * @author Kou Shenhai
 * @version 1.0
 * @date 2020/9/5 0005 上午 12:12
 */
@Component
public class LogApiFeignClientFallbackFactory implements FallbackFactory<LogApiFeignClientFallback> {
    @Override
    public LogApiFeignClientFallback create(Throwable throwable) {
        return new LogApiFeignClientFallback(throwable);
    }
}
