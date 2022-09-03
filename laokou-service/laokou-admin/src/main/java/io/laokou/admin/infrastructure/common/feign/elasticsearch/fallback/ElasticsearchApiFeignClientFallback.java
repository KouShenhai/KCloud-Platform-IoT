package io.laokou.admin.infrastructure.common.feign.elasticsearch.fallback;
import io.laokou.admin.infrastructure.common.feign.elasticsearch.ElasticsearchApiFeignClient;
import io.laokou.admin.infrastructure.common.feign.elasticsearch.form.SearchForm;
import io.laokou.admin.infrastructure.common.feign.elasticsearch.form.SearchVO;
import io.laokou.admin.infrastructure.common.model.CreateIndexModel;
import io.laokou.admin.infrastructure.common.model.ElasticsearchModel;
import io.laokou.common.utils.HttpResultUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
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

    @Override
    public HttpResultUtil<SearchVO<Map<String,Object>>> highlightSearch(SearchForm searchForm) {
        log.error("服务调用失败，报错原因：{}",throwable.getMessage());
        return new HttpResultUtil<SearchVO<Map<String,Object>>>().error("服务调用失败，请联系管理员");
    }

}