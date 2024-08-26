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

package org.laokou.admin.dictItem.ability;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.dictItem.gateway.*;
import org.laokou.admin.dictItem.model.DictItemE;
import org.springframework.stereotype.Component;

/**
 * 字典项领域服务.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DictItemDomainService {

	private final DictItemGateway dictItemGateway;

	public void create(DictItemE dictItemE) {
		dictItemGateway.create(dictItemE);
	}

	public void update(DictItemE dictItemE) {
		dictItemGateway.update(dictItemE);
	}

	public void delete(Long[] ids) {
		dictItemGateway.delete(ids);
	}

}
