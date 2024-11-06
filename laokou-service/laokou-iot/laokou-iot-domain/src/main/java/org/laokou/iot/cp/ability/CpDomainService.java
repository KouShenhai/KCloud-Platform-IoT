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

package org.laokou.iot.cp.ability;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.cp.gateway.*;
import org.laokou.iot.cp.model.CpE;
import org.springframework.stereotype.Component;

/**
 *
 * 通讯协议领域服务.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class CpDomainService {

	private final CpGateway cpGateway;

	public void create(CpE cpE) {
		cpGateway.create(cpE);
	}

	public void update(CpE cpE) {
		cpGateway.update(cpE);
	}

	public void delete(Long[] ids) {
		cpGateway.delete(ids);
	}

}
