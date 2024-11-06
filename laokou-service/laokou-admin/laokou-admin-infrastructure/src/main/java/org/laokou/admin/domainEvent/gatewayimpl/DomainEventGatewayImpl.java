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

package org.laokou.admin.domainEvent.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.domainEvent.convertor.DomainEventConvertor;
import org.laokou.admin.domainEvent.gateway.DomainEventGateway;
import org.laokou.admin.domainEvent.model.DomainEventE;
import org.laokou.common.domain.entity.DomainEventDO;
import org.laokou.common.domain.mapper.DomainEventMapper;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 领域事件网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DomainEventGatewayImpl implements DomainEventGateway {

	private final DomainEventMapper domainEventMapper;

	private final TransactionalUtil transactionalUtil;

	@Override
	public void create(DomainEventE domainEventE) {
		transactionalUtil.executeInTransaction(
				() -> domainEventMapper.insert(DomainEventConvertor.toDataObject(domainEventE, true)));
	}

	@Override
	public void update(DomainEventE domainEventE) {
		DomainEventDO domainEventDO = DomainEventConvertor.toDataObject(domainEventE, false);
		domainEventDO.setVersion(domainEventMapper.selectVersion(domainEventE.getId()));
		update(domainEventDO);
	}

	@Override
	public void delete(Long[] ids) {
		transactionalUtil.executeInTransaction(() -> domainEventMapper.deleteByIds(Arrays.asList(ids)));
	}

	private void update(DomainEventDO domainEventDO) {
		transactionalUtil.executeInTransaction(() -> domainEventMapper.updateById(domainEventDO));
	}

}
