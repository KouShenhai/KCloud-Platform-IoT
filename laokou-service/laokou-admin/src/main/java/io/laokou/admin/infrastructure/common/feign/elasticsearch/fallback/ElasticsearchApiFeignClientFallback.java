package io.laokou.admin.infrastructure.common.feign.elasticsearch.fallback;
import io.laokou.admin.infrastructure.common.feign.elasticsearch.ElasticsearchApiFeignClient;
import io.laokou.admin.infrastructure.common.model.CreateIndexModel;
import io.laokou.admin.infrastructure.common.model.ElasticsearchModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/**
 * 服务降级
 * @author Kou Shenhai
 * @version 1.0
 * @date 2020/9/5 0005 上午 12:12
 */
@Slf4j
@AllArgsConstructor
public class ElasticsearchApiFeignClientFallback implements ElasticsearchApiFeignClient {

    private final Throwable throwable;

    @Override
    public void create(CreateIndexModel model) {
        log.error("服务调用失败，报错原因：{}",throwable.getMessage());
    }

    @Override
    public void syncAsyncBatch(ElasticsearchModel model) {
        log.error("服务调用失败，报错原因：{}",throwable.getMessage());
    }

}
