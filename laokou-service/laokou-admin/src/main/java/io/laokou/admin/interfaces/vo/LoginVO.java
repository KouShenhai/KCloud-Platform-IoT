package io.laokou.admin.interfaces.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2020/12/19 0019 下午 3:21
 */
@Data
@Builder
@ApiModel(value = "登录VO")
@AllArgsConstructor
@NoArgsConstructor
public class LoginVO {

    @ApiModelProperty(value = "令牌",example = "1234567abcdefgABCDEFG")
    private String token;

}
