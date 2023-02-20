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
package org.laokou.tenant.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.mybatisplus.entity.BaseDO;

import java.io.Serial;

/**
 * @author laokou
 */
@Data
@TableName("boot_sys_source")
@Schema(name = "SysSourceDO",description = "系统数据源实体类")
public class SysSourceDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 7616743906900137371L;
    /**
     * 数据源驱动
     */
    @Schema(name = "driverClassName",description = "数据源驱动")
    private String driverClassName;

    /**
     * 数据源连接
     */
    @Schema(name = "url",description = "数据源连接")
    private String url;

    /**
     * 数据源用户名
     */
    @Schema(name = "username",description = "数据源用户名")
    private String username;

    /**
     * 数据源密码
     */
    @Schema(name = "password",description = "数据源密码")
    private String password;

    /**
     * 数据源名称
     */
    @Schema(name = "name",description = "数据源名称")
    private String name;

}
