/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.admin.server.domain.sys.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 系统用户角色
 * @author laokou
 */
@Data
@TableName("boot_sys_user_role")
@Schema(name = "SysUserRoleDO",description = "系统用户角色实体类")
public class SysUserRoleDO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3464670573494984526L;
    /**
     * 角色id
     */
    @Schema(name = "roleId",description = "角色id")
    private Long roleId;

    /**
     * 用户id
     */
    @Schema(name = "userId",description = "用户id")
    private Long userId;

}
