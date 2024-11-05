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

package org.laokou.iot.model.ability;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.model.gateway.*;
import org.laokou.iot.model.model.ModelE;
import org.springframework.stereotype.Component;

/**
 *
 * 模型领域服务.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ModelDomainService {

	private final ModelGateway modelGateway;

	public void create(ModelE modelE) {
		modelGateway.create(modelE);
	}

	public void update(ModelE modelE) {
		modelGateway.update(modelE);
	}

	public void delete(Long[] ids) {
		modelGateway.delete(ids);
	}

}
