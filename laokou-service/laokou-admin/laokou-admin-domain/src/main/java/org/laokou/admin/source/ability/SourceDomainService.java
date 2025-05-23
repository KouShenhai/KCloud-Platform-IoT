/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.source.ability;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.source.gateway.*;
import org.laokou.admin.source.model.SourceE;
import org.springframework.stereotype.Component;

/**
 * 数据源领域服务.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class SourceDomainService {

	private final SourceGateway sourceGateway;

	public void createSource(SourceE sourceE) {
		sourceGateway.createSource(sourceE);
	}

	public void updateSource(SourceE sourceE) {
		sourceGateway.updateSource(sourceE);
	}

	public void deleteSource(Long[] ids) {
		sourceGateway.deleteSource(ids);
	}

}
