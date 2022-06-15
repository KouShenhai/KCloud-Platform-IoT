package io.laokou.admin.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
/**
 * 用户角色管理
 * @author Kou Shenhai
 */
@Data
@TableName("boot_sys_user_role")
@ApiModel("系统用户角色DO")
public class SysUserRoleDO implements Serializable {

    private Long roleId;

    private Long userId;

}
