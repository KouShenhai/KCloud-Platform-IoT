package io.laokou.common.vo;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import lombok.Data;
@Data
public class SysRoleVO {
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    private String name;

    private Integer sort;

}
