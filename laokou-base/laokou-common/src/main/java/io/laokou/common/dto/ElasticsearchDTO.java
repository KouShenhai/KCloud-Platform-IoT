package io.laokou.common.dto;
import lombok.Data;
import java.io.Serializable;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2021/11/1 0001 下午 12:47
 */
@Data
public class ElasticsearchDTO implements Serializable {

    private String data;

    private String indexName;

    private Integer type;

    private String id;

    private String indexAlias;

}
