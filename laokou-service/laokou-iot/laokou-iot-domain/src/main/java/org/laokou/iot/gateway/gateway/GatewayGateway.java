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

package org.laokou.iot.gateway.gateway;

import org.laokou.iot.gateway.model.GatewayE;

/**
 *
 * 网关网关【防腐】.
 *
 * @author laokou
 */
public interface GatewayGateway {

	/**
	 * 新增网关.
	 */
	void createGateway(GatewayE gatewayE);

	/**
	 * 修改网关.
	 */
	void updateGateway(GatewayE gatewayE);

	/**
	 * 删除网关.
	 */
	void deleteGateway(Long[] ids);

	/**
	 * 网关标识是否已存在（修改时排除自身）.
	 */
	boolean existsGatewayKey(Long id, String gatewayKey);

	/**
	 * 网关是否存在.
	 */
	boolean existsGateway(Long id);

	/**
	 * 网关是否全部存在.
	 */
	boolean existsGateway(Long[] ids);

	/**
	 * 产品是否存在.
	 */
	boolean existsProduct(Long productId);

	/**
	 * 查询网关标识.
	 */
	String findGatewayKeyById(Long id);

}
