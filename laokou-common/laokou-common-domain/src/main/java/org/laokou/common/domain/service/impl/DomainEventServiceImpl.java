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

package org.laokou.common.domain.service.impl;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.domain.convertor.DomainEventConvertor;
import org.laokou.common.domain.entity.DomainEventDO;
import org.laokou.common.domain.mapper.DomainEventMapper;
import org.laokou.common.domain.model.DomainEventA;
import org.laokou.common.domain.service.DomainEventService;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Service;

/**
 * @author laokou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DomainEventServiceImpl implements DomainEventService {

	private static final String DOMAIN = "domain";

	private final DomainEventConvertor domainEventConvertor;

	private final DomainEventMapper domainEventMapper;

	private final TransactionalUtil transactionalUtil;

	@Override
	public void create(DomainEventA domainEventA) {
		try {
			DynamicDataSourceContextHolder.push(DOMAIN);
			transactionalUtil.defaultExecuteWithoutResult(r -> {
				try {
					DomainEventDO eventDO = domainEventConvertor.toDataObject(domainEventA);
					domainEventMapper.insert(eventDO);
				}
				catch (Exception e) {
					log.error("错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
					r.setRollbackOnly();
					throw new RuntimeException(LogUtil.record(e.getMessage()));
				}
			});
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

	@Override
	public Long count(Long id) {
		try {
			DynamicDataSourceContextHolder.push(DOMAIN);
			return domainEventMapper
				.selectCount(Wrappers.lambdaQuery(DomainEventDO.class).eq(DomainEventDO::getId, id));
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

}
