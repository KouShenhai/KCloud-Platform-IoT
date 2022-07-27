package io.laokou.admin.interfaces.dto;
import lombok.Data;
import java.util.List;
@Data
public class SysUserDTO {

    private Long id;

    private String username;

    private Integer status;

    private List<Long> roleIds;

    private String password;

    private String imgUrl;

    private String email;

    private String mobile;

    private Long editor;

    private Long deptId;

}
