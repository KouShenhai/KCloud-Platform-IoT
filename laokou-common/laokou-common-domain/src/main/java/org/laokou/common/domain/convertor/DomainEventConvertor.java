/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.domain.repository.DomainEventDO;
import org.laokou.common.i18n.dto.DomainEvent;
import org.laokou.common.i18n.utils.ObjectUtil;

/**
 * @author laokou
 */
public class DomainEventConvertor {

	public static DomainEventDO toDataObject(DomainEvent<Long> event) {
		DomainEventDO domainEventDO = new DomainEventDO();
		domainEventDO.setId(event.getId());
		domainEventDO.setCreateDate(event.getCreateDate());
		domainEventDO.setUpdateDate(event.getUpdateDate());
		domainEventDO.setCreator(event.getCreator());
		domainEventDO.setEditor(event.getEditor());
		domainEventDO.setDeptId(event.getDeptId());
		domainEventDO.setDeptPath(event.getDeptPath());
		domainEventDO.setTenantId(event.getTenantId());
		domainEventDO.setAggregateId(event.getAggregateId());
		domainEventDO
			.setEventStatus(ObjectUtil.isNotNull(event.getEventStatus()) ? event.getEventStatus().name() : null);
		domainEventDO.setEventType(ObjectUtil.isNotNull(event.getEventType()) ? event.getEventType().name() : null);
		domainEventDO.setTopic(event.getTopic());
		domainEventDO.setSourceName(event.getSourceName());
		domainEventDO.setAttribute(JacksonUtil.toJsonStr(event));
		domainEventDO.setAppName(event.getAppName());
		return domainEventDO;
	}

}
