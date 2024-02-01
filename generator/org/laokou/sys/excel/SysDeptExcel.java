package  org.laokou.sys.excel;
import lombok.Data;
import java.io.Serializable;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serial;
/**
* @Description 部门
* @Author laokou
* @Date 2024-02-01
*/
@Data
public class SysDeptExcel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    /**
    * 创建时间
    */
    @ExcelProperty(index = 0,value = "创建时间")
    @ColumnWidth(value = 20)
    private LocalDateTime createDate;

    /**
    * 修改时间
    */
    @ExcelProperty(index = 1,value = "修改时间")
    @ColumnWidth(value = 20)
    private LocalDateTime updateDate;

    /**
    * 删除标识 0未删除 1已删除
    */
    @ExcelProperty(index = 2,value = "删除标识 0未删除 1已删除")
    @ColumnWidth(value = 20)
    private Integer delFlag;

    /**
    * 部门ID
    */
    @ExcelProperty(index = 3,value = "部门ID")
    @ColumnWidth(value = 20)
    private Long deptId;

    /**
    * 部门PATH
    */
    @ExcelProperty(index = 4,value = "部门PATH")
    @ColumnWidth(value = 20)
    private String deptPath;

    /**
    * 租户ID
    */
    @ExcelProperty(index = 5,value = "租户ID")
    @ColumnWidth(value = 20)
    private Long tenantId;

    /**
    * 部门父节点ID
    */
    @ExcelProperty(index = 6,value = "部门父节点ID")
    @ColumnWidth(value = 20)
    private Long pid;

    /**
    * 部门名称
    */
    @ExcelProperty(index = 7,value = "部门名称")
    @ColumnWidth(value = 20)
    private String name;

    /**
    * 部门节点
    */
    @ExcelProperty(index = 8,value = "部门节点")
    @ColumnWidth(value = 20)
    private String path;

    /**
    * 部门排序
    */
    @ExcelProperty(index = 9,value = "部门排序")
    @ColumnWidth(value = 20)
    private Integer sort;

}
