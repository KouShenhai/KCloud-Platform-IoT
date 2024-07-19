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

package org.laokou.admin.gatewayimpl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.convertor.SourceConvertor;
import org.laokou.admin.domain.gateway.SourceGateway;
import org.laokou.admin.domain.source.Source;
import org.laokou.admin.gatewayimpl.database.SourceMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.SourceDO;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 数据源管理.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SourceGatewayImpl implements SourceGateway {

	private final SourceMapper sourceMapper;

	private final TransactionalUtil transactionalUtil;

	private final SourceConvertor sourceConvertor;

	/**
	 * 新增数据源.
	 *
	 * @param source 数据源对象
	 */
	@Override
	public void create(Source source) {
		source.checkName();
		long count = sourceMapper
			.selectCount(Wrappers.lambdaQuery(SourceDO.class).eq(SourceDO::getName, source.getName()));
		source.checkName(count);
		create(sourceConvertor.toDataObject(source));
	}

	/**
	 * 修改数据源.
	 *
	 * @param source 数据源对象
	 */
	@Override
	public void modify(Source source) {
		// source.checkNullId();
		source.checkName();
		long count = sourceMapper.selectCount(Wrappers.lambdaQuery(SourceDO.class)
			.eq(SourceDO::getName, source.getName())
			.ne(SourceDO::getId, source.getId()));
		source.checkName(count);
		SourceDO sourceDO = sourceConvertor.toDataObject(source);
		sourceDO.setVersion(sourceMapper.selectVersion(sourceDO.getId()));
		modify(sourceDO);
	}

	/**
	 * 根据IDS删除数据源.
	 *
	 * @param ids IDS
	 */
	@Override
	public void remove(Long[] ids) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				sourceMapper.deleteByIds(Arrays.asList(ids));
			} catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new SystemException(msg);
			}
		});
	}

	/**
	 * 修改数据源.
	 *
	 * @param sourceDO 数据源数据模型
	 */
	private void modify(SourceDO sourceDO) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				sourceMapper.updateById(sourceDO);
			} catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new SystemException(msg);
			}
		});
	}

	/**
	 * 新增数据源.
	 *
	 * @param sourceDO 数据源数据模型
	 */
	private void create(SourceDO sourceDO) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				sourceMapper.insert(sourceDO);
			} catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new SystemException(msg);
			}
		});
	}

}
