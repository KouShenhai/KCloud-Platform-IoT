package io.laokou.admin.application.service.impl;

import io.laokou.admin.application.service.SysSearchApplicationService;
import io.laokou.admin.infrastructure.common.feign.elasticsearch.ElasticsearchApiFeignClient;
import io.laokou.admin.infrastructure.common.feign.elasticsearch.form.SearchForm;
import io.laokou.admin.infrastructure.common.feign.elasticsearch.form.SearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
public class SysSearchApplicationServiceImpl implements SysSearchApplicationService {

    @Autowired
    private ElasticsearchApiFeignClient elasticsearchApiFeignClient;

    @Override
    public SearchVO<Map<String,Object>> searchResource(SearchForm form) {
        return elasticsearchApiFeignClient.highlightSearch(form).getData();
    }
}
