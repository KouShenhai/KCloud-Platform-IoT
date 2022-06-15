package io.laokou.admin.interfaces.vo;
import lombok.Data;
import java.io.Serializable;
@Data
public class RoleVO implements Serializable {

    private Long id;

    private String name;

    private String tag;

    private Integer sort;

}
