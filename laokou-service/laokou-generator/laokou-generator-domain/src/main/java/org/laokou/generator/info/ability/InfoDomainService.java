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

package org.laokou.generator.info.ability;

import lombok.RequiredArgsConstructor;
import org.laokou.generator.info.gateway.*;
import org.laokou.generator.info.model.InfoE;
import org.springframework.stereotype.Component;

/**
 *
 * 代码生成器信息领域服务.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class InfoDomainService {

	private final InfoGateway infoGateway;

	public void create(InfoE infoE) {
		infoGateway.create(infoE);
	}

	public void update(InfoE infoE) {
		infoGateway.update(infoE);
	}

	public void delete(Long[] ids) {
		infoGateway.delete(ids);
	}

}
