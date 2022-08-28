package io.laokou.admin.infrastructure.common.feign.elasticsearch.factory;
import feign.hystrix.FallbackFactory;
import io.laokou.admin.infrastructure.common.feign.elasticsearch.fallback.ElasticsearchApiFeignClientFallback;
import org.springframework.stereotype.Component;

/**
 * 回调工厂
 * @author Kou Shenhai
 * @version 1.0
 * @date 2020/9/5 0005 上午 12:12
 */
@Component
public class ElasticsearchApiFeignClientFallbackFactory implements FallbackFactory<ElasticsearchApiFeignClientFallback> {
    @Override
    public ElasticsearchApiFeignClientFallback create(Throwable throwable) {
        return new ElasticsearchApiFeignClientFallback(throwable);
    }
}