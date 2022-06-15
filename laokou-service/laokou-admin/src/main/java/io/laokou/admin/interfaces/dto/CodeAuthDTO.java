package io.laokou.admin.interfaces.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
/**
 * 验证码认证DTO
 * @author Kou Shenhai
 */
@Data
@ApiModel(value = "验证码认证DTO")
public class CodeAuthDTO implements Serializable {

    @NotBlank(message = "{sys.user.uuid.require}")
    @ApiModelProperty(value = "唯一标识",name = "uuid",required = true,example = "18974432576")
    private String uuid;

    @ApiModelProperty(value = "验证码",name = "captcha",required = true, example = "551635")
    @NotBlank(message = "{sys.user.captcha.require}")
    private String captcha;

    @ApiModelProperty(value = "验证码类型",name = "type",required = true, example = "1")
    @NotBlank(message = "{sys.user.captcha.type.require}")
    private String type;

}
