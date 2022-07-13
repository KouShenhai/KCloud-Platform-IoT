package io.laokou.admin.interfaces.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* 公众号自定义菜单
*
* @author Kou Shenhai
*/
@Data
@ApiModel(value = "公众号自定义菜单")
public class WXMpMenuDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "菜单json数据")
    private String menu;
    @ApiModelProperty(value = "AppID")
    private String appId;
    @ApiModelProperty(value = "创建者")
    private Long creator;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
    @ApiModelProperty(value = "更新者")
    private Long editor;
    @ApiModelProperty(value = "更新时间")
    private Date updateDate;

}