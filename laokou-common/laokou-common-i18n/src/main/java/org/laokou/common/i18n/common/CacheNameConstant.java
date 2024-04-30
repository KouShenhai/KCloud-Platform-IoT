/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.common.i18n.common;

import io.swagger.v3.oas.annotations.media.Schema;

// @formatter:off
/**
 * name 		=> 缓存名称.
 * ttl 			=> 过期时间，默认为0（不过期）.
 * maxIdleTime 	=> 最大空闲时间（保持存活的最长时间），默认为0 maxSize => 最大长度，默认为0（没有长度限制）.
 * 格式 			=> name_ttl_maxIdleTime_maxSize.
 * @author laokou
 */
// @formatter:on

@Schema(name = "CacheConstants", description = "缓存名称变量")
public final class CacheNameConstant {

	private CacheNameConstant() {
	}

	@Schema(name = "DEFAULT", description = "默认缓存配置")
	private static final String DEFAULT = "_10m_30m_1024";

	@Schema(name = "OSS", description = "OSS缓存配置")
	public static final String OSS = "oss" + DEFAULT;

	@Schema(name = "DEPTS", description = "部门缓存配置")
	public static final String DEPTS = "depts" + DEFAULT;

	@Schema(name = "DICTS", description = "字典缓存配置")
	public static final String DICTS = "dicts" + DEFAULT;

	@Schema(name = "MENUS", description = "菜单缓存配置")
	public static final String MENUS = "menus" + DEFAULT;

	@Schema(name = "MESSAGES", description = "消息缓存配置")
	public static final String MESSAGES = "messages" + DEFAULT;

	@Schema(name = "PACKAGES", description = "套餐缓存配置")
	public static final String PACKAGES = "packages" + DEFAULT;

	@Schema(name = "TENANTS", description = "租户缓存配置")
	public static final String TENANTS = "tenants" + DEFAULT;

	@Schema(name = "SOURCES", description = "数据源缓存配置")
	public static final String SOURCES = "sources" + DEFAULT;

	@Schema(name = "USERS", description = "用户缓存配置")
	public static final String USERS = "users" + DEFAULT;

}
