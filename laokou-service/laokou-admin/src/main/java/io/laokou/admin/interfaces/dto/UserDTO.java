package io.laokou.admin.interfaces.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserDTO implements Serializable {

    private Long id;

    private String username;

    private Integer status;

    private List<Long> roleIds;

    private String password;

    private String imgUrl;

    private Long editor;

}
