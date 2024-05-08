/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import lombok.Data;
import org.laokou.common.mybatisplus.mapper.BaseDO;

import java.io.Serial;

import static org.laokou.common.i18n.common.DSConstant.BOOT_SYS_MENU;

/**
 * 菜单.
 *
 * @author laokou
 */
@Data
@TableName(BOOT_SYS_MENU)
public class MenuDO extends BaseDO {

	@Serial
	private static final long serialVersionUID = 6351930810565072011L;

	/**
	 * 菜单父节点ID.
	 */
	private Long pid;

	/**
	 * 菜单名称.
	 */
	private String name;

	/**
	 * 菜单路由.
	 */
	private String component;

	/**
	 * 菜单权限标识.
	 */
	private String permission;

	/**
	 * 菜单图标。
	 */
	private String icon;

	/**
	 * 菜单排序。
	 */
	private Integer sort;

	/**
	 * 菜单类型 0菜单 1按钮.
	 */
	private Integer type;

	/**
	 * 菜单隐藏 0否 1是.
	 */
	private Boolean hidden;

	/**
	 * 路由缓存 0否 1是.
	 */
	private Boolean keepAlive;

	/**
	 * 菜单链接跳转目标 _blank|_self|_top|_parent.
	 */
	private String target;

	/**
	 * 重定向地址.
	 */
	private String redirect;

	/**
	 * 菜单状态 0正常 1停用.
	 */
	private Integer status;

	/**
	 * 菜单链接地址.
	 */
	private String link;

}
