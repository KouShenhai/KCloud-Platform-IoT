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

package org.laokou.admin.ip.gatewayimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.ip.convertor.IpConvertor;
import org.laokou.admin.ip.gateway.IpGateway;
import org.laokou.admin.ip.gatewayimpl.database.IpMapper;
import org.laokou.admin.ip.gatewayimpl.database.dataobject.IpDO;
import org.laokou.admin.ip.model.IpE;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * IP网关实现.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class IpGatewayImpl implements IpGateway {

	private final IpMapper ipMapper;

	private final TransactionalUtil transactionalUtil;

	public void create(IpE ipE) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				ipMapper.insert(IpConvertor.toDataObject(ipE));
			}
			catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("新增失败，错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new RuntimeException(msg);
			}
		});
	}

	public void update(IpE ipE) {
		IpDO ipDO = IpConvertor.toDataObject(ipE);
		ipDO.setVersion(ipMapper.selectVersion(ipE.getId()));
		update(ipDO);
	}

	public void delete(Long[] ids) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				ipMapper.deleteByIds(Arrays.asList(ids));
			}
			catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("删除失败，错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new RuntimeException(msg);
			}
		});
	}

	private void update(IpDO ipDO) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				ipMapper.updateById(ipDO);
			}
			catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("修改失败，错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new RuntimeException(msg);
			}
		});
	}

}
