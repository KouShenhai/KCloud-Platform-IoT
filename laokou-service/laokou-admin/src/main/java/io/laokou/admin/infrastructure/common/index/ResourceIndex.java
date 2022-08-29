package io.laokou.admin.infrastructure.common.index;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import lombok.Data;
import java.io.Serializable;
@Data
public class ResourceIndex implements Serializable {

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    private String title;

    private String code;

    private String remark;

    private String tags;

    private String ym;
}
