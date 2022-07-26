package io.laokou.admin.domain.sys.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.laokou.common.entity.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/26 0026 下午 3:37
 */
@Data
@TableName("boot_sys_dept")
@ApiModel("部门")
public class SysDeptDO extends BaseDO {

    /**
     * 父部门ID
     */
    @TableField("pid")
    @ApiModelProperty(value = "父部门ID",name = "pid",required = true,example = "0")
    private Long pid;

    /**
     * 状态 0停用 1正常
     */
    @ApiModelProperty(value = "状态 0停用 1正常",name = "status",example = "1")
    @TableField("status")
    private Integer status;

    /**
     * 部门名称
     */
    @TableField("name")
    @ApiModelProperty(value = "部门名称",name = "name",required = true,example = "老寇云")
    private String name;

    @TableField("sort")
    @ApiModelProperty(value = "排序",name = "sort",example = "1")
    private Integer sort;
}
