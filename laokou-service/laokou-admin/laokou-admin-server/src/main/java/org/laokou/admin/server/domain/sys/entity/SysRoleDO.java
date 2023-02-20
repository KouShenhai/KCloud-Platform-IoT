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
import jakarta.validation.constraints.NotBlank;
import org.laokou.common.mybatisplus.entity.BaseDO;
import lombok.Data;

import java.io.Serial;

/**
 * 系统角色
 * @author laokou
 */
@Data
@TableName("boot_sys_role")
@Schema(name = "SysRoleDO",description = "系统角色实体类")
public class SysRoleDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 6034156236048273668L;
    /**
     * 角色名称
     */
    @NotBlank(message = "{sys.role.name.require}")
    @Schema(name = "name",description = "角色名称",example = "管理员")
    private String name;

    /**
     * 角色排序
     */
    @Schema(name = "sort",description = "角色排序",example = "1")
    private Integer sort;

    /**
     * 部门id
     */
    @Schema(name = "deptId",description = "部门id",example = "0")
    private Long deptId;


    /**
     * 租户id
     */
    @Schema(name = "tenantId",description = "租户id")
    private Long tenantId;
}
