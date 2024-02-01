package org.laokou.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import org.laokou.common.mybatisplus.repository.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.io.Serial;
/**
 * <p>
 * 部门
 * </p>
 *
 * @author laokou
 * @since 2024-02-01
 */
@Getter
@Setter
@TableName("boot_sys_dept")
@Schema(name = "SysDeptDO", description = "部门")
public class SysDeptDO extends BaseDO {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "createDate",description = "创建时间")
    @TableField("create_date")
    private LocalDateTime createDate;

    @Schema(name = "updateDate",description = "修改时间")
    @TableField("update_date")
    private LocalDateTime updateDate;

    @Schema(name = "delFlag",description = "删除标识 0未删除 1已删除")
    @TableField("del_flag")
    private Integer delFlag;

    @Schema(name = "deptId",description = "部门ID")
    @TableField("dept_id")
    private Long deptId;

    @Schema(name = "deptPath",description = "部门PATH")
    @TableField("dept_path")
    private String deptPath;

    @Schema(name = "tenantId",description = "租户ID")
    @TableField("tenant_id")
    private Long tenantId;

    @Schema(name = "pid",description = "部门父节点ID")
    @TableField("pid")
    private Long pid;

    @Schema(name = "name",description = "部门名称")
    @TableField("name")
    private String name;

    @Schema(name = "path",description = "部门节点")
    @TableField("path")
    private String path;

    @Schema(name = "sort",description = "部门排序")
    @TableField("sort")
    private Integer sort;
}
