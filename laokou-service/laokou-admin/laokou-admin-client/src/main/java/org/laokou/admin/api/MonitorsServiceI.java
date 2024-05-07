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

package org.laokou.admin.api;

import org.laokou.admin.dto.monitor.clientobject.RedisCacheCO;
import org.laokou.admin.dto.monitor.clientobject.ServerCO;
import org.laokou.common.i18n.dto.Result;

/**
 * 监控管理.
 *
 * @author laokou
 */
public interface MonitorsServiceI {

	/**
	 * 查看缓存监控.
	 * @return 缓存监控
	 */
	Result<RedisCacheCO> findCacheInfo();

	/**
	 * 查看服务器监控.
	 * @return 服务器监控
	 */
	Result<ServerCO> findServerInfo();

}
