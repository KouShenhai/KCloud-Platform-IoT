package io.laokou.admin.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.laokou.common.entity.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 角色管理
 * @author Kou Shenhai
 */
@Data
@TableName("boot_sys_role")
@ApiModel("系统角色DO")
public class SysRoleDO extends BaseDO implements Serializable {

    /**
     * 角色名称
     */
    @NotBlank(message = "{sys.role.name.require}")
    @TableField("name")
    @ApiModelProperty(value = "角色名称",name = "name",required = true,example = "管理员")
    private String name;

    /**
     * 角色标识
     */
    @TableField("tag")
    @ApiModelProperty(value = "角色标识",name = "tag",required = true,example = "admin_role")
    private String tag;

    /**
     * 角色排序
     */
    @TableField("sort")
    @ApiModelProperty(value = "角色排序",name = "sort",required = true,example = "1")
    private Integer sort;

}
