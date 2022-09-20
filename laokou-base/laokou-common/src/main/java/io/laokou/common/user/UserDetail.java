package io.laokou.common.user;
import io.laokou.common.vo.SysDeptVO;
import io.laokou.common.vo.SysRoleVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
/**
 * @author Kou Shenhai
 */
@Data
public class UserDetail implements Serializable {
    private Long id;
    private String username;
    private String imgUrl;
    private Integer superAdmin;
    private Integer status;
    private String email;
    private String mobile;
    private String password;
    private String zfbOpenid;
    private Long deptId;
    private List<String> permissionsList;
    private List<SysRoleVO> roles;
    /**
     * 数据权限
     */
    private List<SysDeptVO> depts;
}
