package org.laokou.test.elasticsearch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.laokou.common.elasticsearch.template.NewElasticsearchTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.List;

@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Slf4j
class ElasticsearchTestApplicationTests {

    private final NewElasticsearchTemplate newElasticsearchTemplate;

    @Test
    void isIndexExists() {
        String indexName = "laokou_trace_202305";
        List<String> indexNames = List.of(indexName);
        log.info("索引是否存在：{}",newElasticsearchTemplate.isIndexExists(indexNames));
        log.info("索引是否存在：{}",newElasticsearchTemplate.isIndexExists(indexName));
    }

}
