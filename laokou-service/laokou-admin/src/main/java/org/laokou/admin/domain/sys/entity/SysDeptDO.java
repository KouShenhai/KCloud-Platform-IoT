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
package org.laokou.admin.domain.sys.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import org.laokou.common.entity.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/26 0026 下午 3:37
 */
@Data
@TableName("boot_sys_dept")
@ApiModel("部门")
public class SysDeptDO extends BaseDO {

    /**
     * 父部门ID
     */
    @TableField("pid")
    @ApiModelProperty(value = "父部门ID",name = "pid",required = true,example = "0")
    private Long pid;

    /**
     * 部门名称
     */
    @TableField("name")
    @ApiModelProperty(value = "部门名称",name = "name",required = true,example = "老寇云集团")
    private String name;

    /**
     * 排序
     */
    @TableField("sort")
    @ApiModelProperty(value = "排序",name = "sort",example = "1")
    private Integer sort;
}
