package io.laokou.admin.interfaces.dto;

import lombok.Data;

@Data
public class SysDeptDTO {

    private Long id;

    private Long pid;

    private String name;

    private Integer sort;

    private Integer status;

}
