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
 * 系统角色菜单
 * @author laokou
 */
@Data
@TableName("boot_sys_role_menu")
@Schema(name = "SysRoleMenuDO",description = "系统角色菜单实体类")
public class SysRoleMenuDO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4300621783981688988L;
    /**
     * 菜单id
     */
    @Schema(name = "menuId",description = "菜单id")
    private Long menuId;

    /**
     * 角色id
     */
    @Schema(name = "roleId",description = "角色id")
    private Long roleId;

}
