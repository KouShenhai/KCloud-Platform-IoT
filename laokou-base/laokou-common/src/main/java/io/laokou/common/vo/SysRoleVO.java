package io.laokou.common.vo;
import lombok.Data;

import java.io.Serializable;

@Data
public class SysRoleVO implements Serializable {
    private Long id;

    private String name;

    private Integer sort;

}
