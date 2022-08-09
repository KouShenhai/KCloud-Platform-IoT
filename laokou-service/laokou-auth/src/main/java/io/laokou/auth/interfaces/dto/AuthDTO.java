package io.laokou.auth.interfaces.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthDTO {

    @NotBlank(message = "{sys.user.username.require}")
    @ApiModelProperty(value = "用户名",name = "username",required = true,example = "admin")
    private String username;

    @NotBlank(message = "{sys.user.password.require}")
    @ApiModelProperty(value = "密码",name = "password",required = true,example = "123456")
    private String password;

    @NotBlank(message = "回调地址不能为空")
    private String redirectUrl;

}
