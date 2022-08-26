package io.laokou.common.index;
import io.laokou.common.annotation.ElasticsearchFieldInfo;
import lombok.Data;
import java.io.Serializable;
@Data
public class ResourceIndex implements Serializable {

    @ElasticsearchFieldInfo(type = "long")
    private String id;

    @ElasticsearchFieldInfo(participle = 3)
    private String title;

    @ElasticsearchFieldInfo
    private String uri;

    @ElasticsearchFieldInfo
    private String code;

    @ElasticsearchFieldInfo(participle = 3)
    private String remark;

    @ElasticsearchFieldInfo(participle = 3)
    private String tags;

}
