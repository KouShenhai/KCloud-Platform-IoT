/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import org.laokou.common.mybatisplus.mapper.BaseDO;

import java.io.Serial;

import static org.laokou.common.i18n.common.DatasourceConstant.BOOT_SYS_MENU;

/**
 * @author laokou
 */
@Data
@TableName(BOOT_SYS_MENU)
@Schema(name = "MenuDO", description = "菜单")
public class MenuDO extends BaseDO {

	@Serial
	private static final long serialVersionUID = 6351930810565072011L;

	@Schema(name = "pid", description = "菜单父节点ID", example = "0")
	private Long pid;

	@Schema(name = "name", description = "菜单名称", example = "用户管理")
	private String name;

	@Schema(name = "url", description = "菜单URL", example = "/v1/users/profile")
	private String url;

	@Schema(name = "permission", description = "菜单权限标识", example = "users:list")
	private String permission;

	@Schema(name = "icon", description = "菜单图标", example = "user")
	private String icon;

	@Schema(name = "sort", description = "菜单排序", example = "1")
	private Integer sort;

	@Schema(name = "type", description = "菜单类型 0菜单 1按钮", example = "0")
	private Integer type;

	@Schema(name = "visible", description = "菜单状态 0显示 1隐藏", example = "0")
	private Integer visible;

}
