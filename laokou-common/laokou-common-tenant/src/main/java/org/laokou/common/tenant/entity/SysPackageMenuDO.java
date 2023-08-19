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

package org.laokou.common.tenant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.mybatisplus.database.dataobject.BaseDO;

import java.io.Serial;

/**
 * @author laokou
 */
@Data
@TableName("boot_sys_package_menu")
@Schema(name = "SysPackageMenuDO", description = "系统套餐菜单实体类")
public class SysPackageMenuDO extends BaseDO {

	@Serial
	private static final long serialVersionUID = 683935513335526615L;

	/**
	 * 套餐id
	 */
	@Schema(name = "packageId", description = "套餐id")
	private Long packageId;

	/**
	 * 菜单id
	 */
	@Schema(name = "menuId", description = "菜单id")
	private Long menuId;

}
