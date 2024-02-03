package  org.laokou.sys.dto;
import lombok.Data;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
/**
 * @Description 字典
 * @Author laokou
 * @Date 2024-02-01
 */
@Data
public class SysDictDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "id",description = "主键")
    private String id;
    /**
    * 创建时间
    */
    @Schema(name = "createDate",description = "创建时间")
    private LocalDateTime createDate;
    /**
    * 修改时间
    */
    @Schema(name = "updateDate",description = "修改时间")
    private LocalDateTime updateDate;
    /**
    * 删除标识 0未删除 1已删除
    */
    @Schema(name = "delFlag",description = "删除标识 0未删除 1已删除")
    private Integer delFlag;
    /**
    * 部门ID
    */
    @Schema(name = "deptId",description = "部门ID")
    private Long deptId;
    /**
    * 部门PATH
    */
    @Schema(name = "deptPath",description = "部门PATH")
    private String deptPath;
    /**
    * 租户ID
    */
    @Schema(name = "tenantId",description = "租户ID")
    private Long tenantId;
    /**
    * 字典标签
    */
    @Schema(name = "label",description = "字典标签")
    private String label;
    /**
    * 字典值
    */
    @Schema(name = "value",description = "字典值")
    private String value;
    /**
    * 字典类型
    */
    @Schema(name = "type",description = "字典类型")
    private String type;
    /**
    * 字典备注
    */
    @Schema(name = "remark",description = "字典备注")
    private String remark;
    /**
    * 字典排序
    */
    @Schema(name = "sort",description = "字典排序")
    private Integer sort;

}
