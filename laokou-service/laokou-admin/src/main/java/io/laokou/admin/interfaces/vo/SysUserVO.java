package io.laokou.admin.interfaces.vo;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import io.laokou.common.utils.DateUtil;
import lombok.Data;
import java.util.Date;
import java.util.List;
@Data
public class SysUserVO {
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;
    @JSONField(format = DateUtil.DATE_TIME_PATTERN)
    private Date createDate;
    private String username;
    private String imgUrl;
    private Long deptId;
    private Integer superAdmin;
    private Integer status;
    private List<Long> roleIds;
}
