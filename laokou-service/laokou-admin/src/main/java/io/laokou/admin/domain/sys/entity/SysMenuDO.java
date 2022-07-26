package io.laokou.admin.domain.sys.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.laokou.common.entity.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
/**
 * 菜单管理
 *
 * @author Kou Shenhai
 */
@Data
@TableName("boot_sys_menu")
@ApiModel("菜单")
public class SysMenuDO extends BaseDO {

	/**
	 * 父菜单ID，一级菜单为0
	 */
	@NotBlank(message = "{sys.menu.pid.require}")
    @TableField("pid")
    @ApiModelProperty(value = "父菜单ID",name = "pid",required = true,example = "0")
	private Long pid;

	/**
	 * 菜单名称
	 */
    @NotBlank(message = "{sys.menu.name.require}")
    @TableField("name")
    @ApiModelProperty(value = "菜单名称",name = "name",required = true,example = "用户管理")
	private String name;

	/**
	 * 菜单URL
	 */
    @TableField("url")
    @ApiModelProperty(value = "菜单URL",name = "url", example = "/sys/user/api/login")
	private String url;

	/**
	 * 授权(多个用逗号分隔，如：sys:user:list,sys:user:save)
	 */
    @TableField("permissions")
    @ApiModelProperty(value = "授权(多个用逗号分隔，如：sys:user:list,sys:user:save)",name = "permissions",required = false,example = "sys:res:view")
	private String permissions;

	/**
	 * 类型   0：菜单   1：按钮
	 */
    @TableField("type")
    @ApiModelProperty(value = "类型   0：菜单   1：按钮",name = "type",example = "0")
	private Integer type;

    @TableField("auth_level")
	@ApiModelProperty(value = "认证等级   0：权限认证   1：登录认证    2：无需认证",name = "auth_level",required = true,example = "0")
	private Integer authLevel;

	@TableField("method")
	@ApiModelProperty(value = "请求方式（如：GET、POST、PUT、DELETE）",name = "method",required = true,example = "GET")
	private String method;

	@TableField("icon")
	@ApiModelProperty(value = "图标",name = "icon",example = "user")
	private String icon;

	@TableField("sort")
	@ApiModelProperty(value = "排序",name = "sort",example = "1")
	private Integer sort;

}
