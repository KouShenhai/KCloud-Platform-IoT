package io.laokou.admin.infrastructure.common.feign.elasticsearch;
import io.laokou.admin.infrastructure.common.feign.elasticsearch.factory.ElasticsearchApiFeignClientFallbackFactory;
import io.laokou.admin.infrastructure.common.model.CreateIndexModel;
import io.laokou.admin.infrastructure.common.model.ElasticsearchModel;
import io.laokou.common.constant.ServiceConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
/**
 * @author Kou Shenhai
 */
@FeignClient(contextId = ServiceConstant.LAOKOU_ELASTICSEARCH,url = "${service.elasticsearch.uri}",name = ServiceConstant.LAOKOU_ELASTICSEARCH, fallbackFactory = ElasticsearchApiFeignClientFallbackFactory.class)
@Service
public interface ElasticsearchApiFeignClient {

    @PostMapping("/api/create")
    void create(@RequestBody final CreateIndexModel model);

    @PostMapping("/api/syncAsyncBatch")
    void syncAsyncBatch(@RequestBody final ElasticsearchModel model);

}
