package io.laokou.admin.infrastructure.common.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Model
 * @author Kou Shenhai
 * @version 1.0
 * @date 2021/11/1 0001 下午 12:45
 */
@Data
public class ElasticsearchModel implements Serializable {
    private String data;
    private String indexName;
    private String indexAlias;
    private String id;
}
