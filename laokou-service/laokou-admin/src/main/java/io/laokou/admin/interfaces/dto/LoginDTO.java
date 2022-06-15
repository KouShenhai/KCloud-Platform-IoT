package io.laokou.admin.interfaces.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
/**
 * 账号密码DTO
 * @author Kou Shenhai
 */
@Data
@ApiModel(value = "账号密码DTO")
public class LoginDTO implements Serializable {

    @NotBlank(message = "{sys.user.username.require}")
    @ApiModelProperty(value = "用户名",name = "username",required = true,example = "admin")
    private String username;

    @NotBlank(message = "{sys.user.password.require}")
    @ApiModelProperty(value = "密码",name = "password",required = true,example = "123456")
    private String password;

    @NotBlank(message = "{sys.user.uuid.require}")
    @ApiModelProperty(value = "唯一标识",name = "uuid",required = true,example = "66666666-6666-6666-6666-666666666666")
    private String uuid;

    @NotBlank(message = "{sys.user.captcha.require}")
    @ApiModelProperty(value = "验证码",name = "captcha",required = true, example = "x84cc")
    private String captcha;

}
