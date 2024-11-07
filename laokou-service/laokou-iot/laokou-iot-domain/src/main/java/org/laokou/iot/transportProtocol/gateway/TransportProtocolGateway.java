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

package org.laokou.iot.transportProtocol.gateway;

import org.laokou.iot.transportProtocol.model.TransportProtocolE;

/**
 *
 * 传输协议网关【防腐】.
 *
 * @author laokou
 */
public interface TransportProtocolGateway {

	/**
	 * 新增传输协议.
	 */
	void create(TransportProtocolE transportProtocolE);

	/**
	 * 修改传输协议.
	 */
	void update(TransportProtocolE transportProtocolE);

	/**
	 * 删除传输协议.
	 */
	void delete(Long[] ids);

}
