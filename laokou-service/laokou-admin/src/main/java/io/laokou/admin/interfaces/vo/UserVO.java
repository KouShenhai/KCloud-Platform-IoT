package io.laokou.admin.interfaces.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class UserVO implements Serializable {

    private Long id;

    private Date createDate;

    private String username;

    private String imgUrl;

    private Integer superAdmin;

    private Integer status;

    private List<Long> roleIds;

}
