package io.laokou.admin.interfaces.vo;
import io.laokou.common.utils.DateUtil;
import lombok.Data;
import java.util.Date;
import java.util.List;
@Data
public class SysUserVO {
    private Long id;
    private Date createDate;
    private String username;
    private String imgUrl;
    private Long deptId;
    private Integer superAdmin;
    private Integer status;
    private List<Long> roleIds;
}
