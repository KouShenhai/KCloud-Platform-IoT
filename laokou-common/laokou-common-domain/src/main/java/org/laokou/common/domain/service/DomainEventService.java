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

package org.laokou.common.domain.service;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.ibatis.session.ResultHandler;
import org.laokou.common.domain.database.dataobject.DomainEventDO;
import org.laokou.common.i18n.dto.DomainEvent;

import java.util.List;
import java.util.Set;

/**
 * @author laokou
 */
@Schema(name = "DomainEventService", description = "领域事件")
public interface DomainEventService {

	void create(List<DomainEvent<Long>> events);

	void modify(List<DomainEvent<Long>> events);

	void removeLastMonth(String ymd);

	void findList(Set<String> sourceNames, String appName, ResultHandler<DomainEventDO> resultHandler);

}
