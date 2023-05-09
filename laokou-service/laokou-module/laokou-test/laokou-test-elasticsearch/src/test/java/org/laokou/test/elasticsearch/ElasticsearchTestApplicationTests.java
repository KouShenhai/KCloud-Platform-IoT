package org.laokou.test.elasticsearch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.elasticsearch.template.NewElasticsearchTemplate;
import org.laokou.common.elasticsearch.utils.FieldMapping;
import org.laokou.common.elasticsearch.utils.FieldMappingUtil;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.List;

@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Slf4j
class ElasticsearchTestApplicationTests {

    private final NewElasticsearchTemplate newElasticsearchTemplate;

    private ResourceIndex resourceIndex;

    @Before
    void before() {
        resourceIndex = new ResourceIndex();
        resourceIndex.setId(1L);
        resourceIndex.setRemark("33");
        resourceIndex.setCode("123");
        resourceIndex.setTitle("哈哈哈");
    }

    @Test
    void isIndexExists() {
        String indexName = "laokou_trace_202305";
        List<String> indexNames = List.of(indexName);
        log.info("索引是否存在：{}",newElasticsearchTemplate.isIndexExists(indexNames));
        log.info("索引是否存在：{}",newElasticsearchTemplate.isIndexExists(indexName));
    }

    @Test
    void createIndex() {
        String indexName = "laokou_test_202305";
        newElasticsearchTemplate.createIndex(indexName,"laokou_test", ResourceIndex.class);
    }

    @Test
    void mapping() {
        List<FieldMapping> fieldInfo = FieldMappingUtil.getFieldInfo(ResourceIndex.class);
        log.info("{}", JacksonUtil.toJsonStr(fieldInfo));
    }

}
