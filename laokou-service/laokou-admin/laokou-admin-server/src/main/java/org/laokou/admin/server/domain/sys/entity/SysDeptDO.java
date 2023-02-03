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
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import org.laokou.common.mybatisplus.entity.BaseDO;
import lombok.Data;
/**
 * 系统部门
 * @author laokou
 */
@Data
@TableName("boot_sys_dept")
@Schema(name = "SysDeptDO",description = "系统部门实体类")
public class SysDeptDO extends BaseDO {

    /**
     * 父部门ID
     */
    @TableField("pid")
    @Schema(name = "pid",description = "父部门ID",example = "0")
    private Long pid;

    /**
     * 部门名称
     */
    @TableField("name")
    @Schema(name = "name",description = "部门名称",example = "老寇云集团")
    private String name;

    /**
     * 排序
     */
    @TableField("sort")
    @Schema(name = "sort",description = "部门排序",example = "1")
    private Integer sort;

    /**
     * 路径
     */
    @TableField("path")
    @Schema(name = "path",description = "部门路径",example = "0")
    private String path;

    /**
     * 租户id
     */
    @Schema(name = "tenantId",description = "租户id")
    private Long tenantId;

}
