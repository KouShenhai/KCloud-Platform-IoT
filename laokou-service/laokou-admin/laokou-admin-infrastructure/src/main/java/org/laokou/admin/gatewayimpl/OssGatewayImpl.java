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

package org.laokou.admin.gatewayimpl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.common.event.DomainEventPublisher;
import org.laokou.admin.convertor.OssConvertor;
import org.laokou.admin.domain.gateway.OssGateway;
import org.laokou.admin.domain.oss.Oss;
import org.laokou.admin.domain.oss.OssLog;
import org.laokou.admin.gatewayimpl.database.OssMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.OssDO;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * OSS管理.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OssGatewayImpl implements OssGateway {

	private final OssMapper ossMapper;

	private final TransactionalUtil transactionalUtil;

	private final DomainEventPublisher domainEventPublisher;

	private final OssConvertor ossConvertor;

	/**
	 * 新增OSS.
	 * @param oss OSS对象
	 */
	@Override
	public void create(Oss oss) {
		long count = ossMapper.selectCount(Wrappers.lambdaQuery(OssDO.class).eq(OssDO::getName, oss.getName()));
		oss.checkName(count);
		OssDO ossDO = ossConvertor.toDataObject(oss);
		create(ossDO);
	}

	/**
	 * 修改OSS.
	 * @param oss OSS对象
	 */
	@Override
	public void modify(Oss oss) {
		oss.checkNullId();
		long count = ossMapper.selectCount(
				Wrappers.lambdaQuery(OssDO.class).eq(OssDO::getName, oss.getName()).ne(OssDO::getId, oss.getId()));
		oss.checkName(count);
		OssDO ossDO = ossConvertor.toDataObject(oss);
		ossDO.setVersion(ossMapper.selectVersion(ossDO.getId()));
		modify(ossDO);
	}

	/**
	 * 根据IDS删除OSS.
	 * @param ids IDS
	 */
	@Override
	public void remove(Long[] ids) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				ossMapper.deleteBatchIds(Arrays.asList(ids));
			}
			catch (Exception e) {
				String msg = LogUtil.result(e.getMessage());
				log.error("错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new SystemException(msg);
			}
		});
	}

	/**
	 * 推送OSS日志事件.
	 * @param ossLog OSS日志对象
	 */
	@Override
	public void publish(OssLog ossLog) {
		domainEventPublisher.publish(null);
	}

	/**
	 * 转换OSS日志事件.
	 * @param ossLog OSS日志对象
	 * @return OSS日志事件
	 */
	/*
	 * private OssLogEvent getEvent(OssLog ossLog) { OssLogEvent event = null;
	 * event.setMd5(ossLog.getMd5()); event.setUrl(ossLog.getUrl());
	 * event.setName(ossLog.getName()); event.setSize(ossLog.getSize()); return event; }
	 */

	/**
	 * 新增OSS.
	 * @param ossDO OSS数据模型
	 */
	private void create(OssDO ossDO) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				ossMapper.insert(ossDO);
			}
			catch (Exception e) {
				String msg = LogUtil.result(e.getMessage());
				log.error("错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new SystemException(msg);
			}
		});
	}

	/**
	 * 修改OSS.
	 * @param ossDO OSS数据模型
	 */
	private void modify(OssDO ossDO) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				ossMapper.updateById(ossDO);
			}
			catch (Exception e) {
				String msg = LogUtil.result(e.getMessage());
				log.error("错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new SystemException(msg);
			}
		});
	}

}
