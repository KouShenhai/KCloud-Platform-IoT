/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
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
package io.laokou.admin.domain.sys.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.laokou.common.entity.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
/**
 * 角色管理
 * @author Kou Shenhai
 */
@Data
@TableName("boot_sys_role")
@ApiModel("系统角色DO")
public class SysRoleDO extends BaseDO {

    /**
     * 角色名称
     */
    @NotBlank(message = "{sys.role.name.require}")
    @TableField("name")
    @ApiModelProperty(value = "角色名称",name = "name",required = true,example = "管理员")
    private String name;

    /**
     * 角色排序
     */
    @TableField("sort")
    @ApiModelProperty(value = "角色排序",name = "sort",required = true,example = "1")
    private Integer sort;

    /**
     * 部门id
     */
    @TableField("dept_id")
    @ApiModelProperty(value = "部门id",name = "deptId",required = true,example = "0")
    private Long deptId;

}
