/**
 * Copyright 2020-2022 Kou Shenhai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.laokou.auth.interfaces.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 账号密码DTO
 * @author Kou Shenhai
 */
@Data
@ApiModel(value = "账号密码DTO")
public class LoginDTO {

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
