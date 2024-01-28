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

package org.laokou.common.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.laokou.common.domain.repository.DomainEventMapper;
import org.laokou.common.domain.service.DomainEventService;
import org.laokou.common.i18n.dto.DomainEvent;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class DomainEventServiceImpl implements DomainEventService {

	private final DomainEventMapper domainEventMapper;

	@Override
	public void create(List<DomainEvent<Long>> events) {

	}

	@Override
	public void modify(DomainEvent<Long> event) {

	}

	@Override
	public void remove(Long id) {

	}

	@Override
	public void find() {

	}

}
