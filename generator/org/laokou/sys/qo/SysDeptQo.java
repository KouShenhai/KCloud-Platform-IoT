package  org.laokou.sys.qo;
import lombok.Data;
import org.laokou.common.i18n.dto.BasePage;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
/**
 * @Description 部门
 * @Author laokou
 * @Date 2024-02-01
 */
@Data
public class SysDeptQo extends BasePage {

    @Serial
    private static final long serialVersionUID = 1L;

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
    * 部门父节点ID
    */
    @Schema(name = "pid",description = "部门父节点ID")
    private Long pid;

    /**
    * 部门名称
    */
    @Schema(name = "name",description = "部门名称")
    private String name;

    /**
    * 部门节点
    */
    @Schema(name = "path",description = "部门节点")
    private String path;

    /**
    * 部门排序
    */
    @Schema(name = "sort",description = "部门排序")
    private Integer sort;
}
