package io.laokou.datasource.feign.admin.factory;
import feign.hystrix.FallbackFactory;
import io.laokou.datasource.feign.admin.fallback.AuthApiFeignClientFallback;
import org.springframework.stereotype.Component;

/**
 * 回调工厂
 * @author Kou Shenhai
 * @version 1.0
 * @date 2020/9/5 0005 上午 12:12
 */
@Component
public class AuthApiFeignClientFallbackFactory implements FallbackFactory<AuthApiFeignClientFallback> {
    @Override
    public AuthApiFeignClientFallback create(Throwable throwable) {
        return new AuthApiFeignClientFallback(throwable);
    }
}
