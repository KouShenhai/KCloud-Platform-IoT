/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.data.cache.constant;

// @formatter:off
/**
 * 命名格式如下：
 * name 		=> 缓存名称.
 * ttl 			=> 过期时间，默认为0（不过期）.
 * maxIdleTime 	=> 最大空闲时间（保持存活的最长时间），默认为0.
 * maxSize      => 最大长度，默认为0（没有长度限制）.
 * 格式 			=> name_ttl_maxIdleTime_maxSize.
 * @author laokou
 */
// @formatter:on
public final class NameConstants {

	/**
	 * OSS缓存配置.
	 */
	public static final String OSS = "oss";

	/**
	 * 部门缓存配置.
	 */
	public static final String DEPTS = "depts";

	/**
	 * 字典缓存配置.
	 */
	public static final String DICTS = "dicts";

	/**
	 * 菜单缓存配置.
	 */
	public static final String MENUS = "menus";

	/**
	 * 消息缓存配置.
	 */
	public static final String MESSAGES = "messages";

	/**
	 * 套餐缓存配置.
	 */
	public static final String PACKAGES = "packages";

	/**
	 * 租户缓存配置.
	 */
	public static final String TENANTS = "tenants";

	/**
	 * 数据源缓存配置.
	 */
	public static final String SOURCES = "sources";

	/**
	 * 用户缓存配置.
	 */
	public static final String USERS = "users";

	/**
	 * 用户菜单缓存配置.
	 */
	public static final String USER_MENU = "user_menu";

	/**
	 * OSS日志缓存配置.
	 */
	public static final String OSS_LOG = "oss_log";

	/**
	 * OSS资源缓存配置.
	 */
	public static final String OSS_RESOURCE = "oss_resource";

	private NameConstants() {
	}

}
