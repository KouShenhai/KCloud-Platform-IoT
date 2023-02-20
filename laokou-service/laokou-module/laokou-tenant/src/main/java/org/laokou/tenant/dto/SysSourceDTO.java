/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.tenant.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author laokou
 */
@Data
public class SysSourceDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -2121545002296157366L;
    private Long id;
    @NotBlank(message = "数据源名称不为空")
    private String name;
    @NotBlank(message = "数据源驱动不为空")
    private String driverClassName;
    @NotBlank(message = "数据源用户名不为空")
    private String username;
    @NotBlank(message = "数据源密码不为空")
    private String password;
    @NotBlank(message = "数据源连接不为空")
    private String url;

}
