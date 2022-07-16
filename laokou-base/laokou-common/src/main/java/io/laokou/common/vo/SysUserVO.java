package io.laokou.common.vo;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import lombok.Data;
import java.util.Date;
import java.util.List;
@Data
public class SysUserVO {
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;
    private Date createDate;
    private String username;
    private String imgUrl;
    private Integer superAdmin;
    private Integer status;
    private List<Long> roleIds;
}
