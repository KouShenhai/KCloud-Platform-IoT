package io.laokou.admin.interfaces.dto;
import lombok.Data;
import java.util.List;
@Data
public class SysRoleDTO {

    private Long id;

    private String name;

    private Integer sort;

    private List<Long> menuIds;

    private List<Long> deptIds;

}
