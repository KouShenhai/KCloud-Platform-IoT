package io.laokou.admin.interfaces.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MenuDTO implements Serializable {

    /**
     * 类型   0：菜单   1：按钮
     */
    private Integer type;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 资源URL
     */
    private String url;
    /**
     * 请求方式（如：GET、POST、PUT、DELETE）
     */
    private String method;
    /**
     * 认证等级   0：权限认证   1：登录认证    2：无需认证
     */
    private Integer authLevel;
    /**
     * 权限标识
     */
    private String permissions;

    private Long id;

    private String name;

    private Long pid;

    private String icon;

}
