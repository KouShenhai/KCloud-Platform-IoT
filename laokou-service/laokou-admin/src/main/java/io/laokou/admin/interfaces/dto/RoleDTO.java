package io.laokou.admin.interfaces.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RoleDTO implements Serializable {

    private Long id;

    private String name;

    private Integer sort;

    private List<Long> menuIds;

}
