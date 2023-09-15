/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
 *
 */
package org.laokou.admin.gatewayimpl.database.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.mybatisplus.database.dataobject.BaseDO;

import java.io.Serial;

/**
 * @author laokou
 */
@Data
@TableName("boot_sys_dept")
@Schema(name = "DeptDO", description = "部门")
public class DeptDO extends BaseDO {

	@Serial
	private static final long serialVersionUID = 5119306834026407994L;

	@Schema(name = "pid", description = "父ID", example = "0")
	private Long pid;

	@Schema(name = "name", description = "名称", example = "老寇云集团")
	private String name;

	@Schema(name = "path", description = "部门节点")
	private String path;

	@Schema(name = "sort", description = "排序", example = "1")
	private Integer sort;

}
