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
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.domainEvent.convertor.DomainEventConvertor;
import org.laokou.admin.domainEvent.gateway.DomainEventGateway;
import org.laokou.admin.domainEvent.model.DomainEventE;
import org.laokou.common.domain.entity.DomainEventDO;
import org.laokou.common.domain.mapper.DomainEventMapper;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 领域事件网关实现.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DomainEventGatewayImpl implements DomainEventGateway {

	private final DomainEventMapper domainEventMapper;
	private final TransactionalUtil transactionalUtil;

	public void create(DomainEventE domainEventE) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				domainEventMapper.insert(DomainEventConvertor.toDataObject(domainEventE, true));
			} catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("新增失败，错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new RuntimeException(msg);
			}
		});
	}

	public void update(DomainEventE domainEventE) {
		DomainEventDO domainEventDO = DomainEventConvertor.toDataObject(domainEventE, false);
		domainEventDO.setVersion(domainEventMapper.selectVersion(domainEventE.getId()));
		update(domainEventDO);
	}

	public void delete(Long[] ids) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				domainEventMapper.deleteByIds(Arrays.asList(ids));
			} catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("删除失败，错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new RuntimeException(msg);
			}
		});
	}

	private void update(DomainEventDO domainEventDO) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				domainEventMapper.updateById(domainEventDO);
			} catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("修改失败，错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new RuntimeException(msg);
			}
		});
	}

}
