package io.laokou.admin.domain.sys.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
/**
 * 角色菜单管理
 * @author Kou Shenhai
 */
@Data
@TableName("boot_sys_role_menu")
@ApiModel("系统角色菜单DO")
public class SysRoleMenuDO {

    private Long menuId;
    private Long roleId;

}
