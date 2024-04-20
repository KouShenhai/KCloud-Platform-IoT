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
import org.apache.ibatis.session.ResultHandler;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.domain.context.DomainEventContextHolder;
import org.laokou.common.domain.convertor.DomainEventConvertor;
import org.laokou.common.domain.database.dataobject.DomainEventDO;
import org.laokou.common.domain.database.DomainEventMapper;
import org.laokou.common.domain.service.DomainEventService;
import org.laokou.common.i18n.dto.DomainEvent;
import org.laokou.common.mybatisplus.utils.MybatisUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class DomainEventServiceImpl implements DomainEventService {

	private final DomainEventMapper domainEventMapper;

	private final MybatisUtil mybatisUtil;

	@Override
	public void create(List<DomainEvent<Long>> events) {
		if (CollectionUtil.isNotEmpty(events)) {
			List<DomainEventDO> list = events.stream().map(DomainEventConvertor::toDataObject).toList();
			mybatisUtil.batch(list, DomainEventMapper.class, events.getFirst().getSourceName(),
					DomainEventMapper::insertOne);
			DomainEventContextHolder.set(events);
		}
	}

	@Override
	public void modify(List<DomainEvent<Long>> events) {
		List<DomainEventDO> list = events.stream().map(DomainEventConvertor::toDataObject).toList();
		mybatisUtil.batch(list, DomainEventMapper.class, events.getFirst().getSourceName(),
				DomainEventMapper::updateStatus);
	}

	@Override
	public void removeLastMonth(String ymd) {
		domainEventMapper.deleteByYmd(ymd);
	}

	@Override
	public void findList(Set<String> sourceNames, String appName, ResultHandler<DomainEventDO> resultHandler) {
		// 查询 创建/发布成功/发布失败/消费失败
		if (domainEventMapper.selectTotal(sourceNames, appName) > 0) {
			domainEventMapper.selectObjects(sourceNames, appName, resultHandler);
		}
	}

}
