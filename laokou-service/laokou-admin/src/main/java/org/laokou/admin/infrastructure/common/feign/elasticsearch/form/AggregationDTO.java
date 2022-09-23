package org.laokou.admin.infrastructure.common.feign.elasticsearch.form;

import lombok.Data;

import java.io.Serializable;

@Data
public class AggregationDTO implements Serializable {

    private String groupKey;
    private String field;
    private String script;

}
