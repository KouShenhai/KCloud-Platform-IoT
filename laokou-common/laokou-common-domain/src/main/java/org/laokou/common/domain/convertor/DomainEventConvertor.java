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

package org.laokou.common.domain.convertor;

import org.laokou.common.domain.entity.DomainEventE;
import org.laokou.common.domain.mapper.DomainEventDO;

/**
 * @author laokou
 */
public class DomainEventConvertor {

	public static DomainEventDO toDataObject(DomainEventE domainEventA) {
		DomainEventDO domainEventDO = new DomainEventDO();
		domainEventDO.setEventType(domainEventA.getEventType());
		domainEventDO.setTopic(domainEventA.getTopic());
		domainEventDO.setTag(domainEventA.getTag());
		domainEventDO.setAggregateId(domainEventA.getAggregateId());
		domainEventDO.setAttribute(domainEventA.getAttribute());
		return domainEventDO;
	}

}
