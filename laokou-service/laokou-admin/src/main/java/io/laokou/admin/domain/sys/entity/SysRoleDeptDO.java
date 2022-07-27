package io.laokou.admin.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/27 0027 上午 9:20
 */
@Data
@TableName("boot_sys_role_dept")
@ApiModel("系统角色部门DO")
public class SysRoleDeptDO {

    private Long roleId;
    private Long deptId;

}
