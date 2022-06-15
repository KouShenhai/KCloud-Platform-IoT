package io.laokou.admin.interfaces.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
public class LoginVO implements Serializable {

    @ApiModelProperty(value = "授权码（token）",example = "1234567abcdefgABCDEFG")
    @JsonProperty("Authorization")
    private String Authorization;

}
