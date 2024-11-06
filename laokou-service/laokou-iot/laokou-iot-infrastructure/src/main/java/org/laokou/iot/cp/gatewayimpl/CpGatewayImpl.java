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

package org.laokou.iot.cp.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.cp.model.CpE;
import org.springframework.stereotype.Component;
import org.laokou.iot.cp.gateway.CpGateway;
import org.laokou.iot.cp.gatewayimpl.database.CpMapper;
import java.util.Arrays;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.iot.cp.convertor.CpConvertor;
import org.laokou.iot.cp.gatewayimpl.database.dataobject.CpDO;

/**
 *
 * 通讯协议网关实现.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CpGatewayImpl implements CpGateway {

	private final CpMapper cpMapper;

	private final TransactionalUtil transactionalUtil;

	public void create(CpE cpE) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				cpMapper.insert(CpConvertor.toDataObject(cpE, true));
			}
			catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("新增失败，错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new RuntimeException(msg);
			}
		});
	}

	public void update(CpE cpE) {
		CpDO cpDO = CpConvertor.toDataObject(cpE, false);
		cpDO.setVersion(cpMapper.selectVersion(cpE.getId()));
		update(cpDO);
	}

	public void delete(Long[] ids) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				cpMapper.deleteByIds(Arrays.asList(ids));
			}
			catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("删除失败，错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new RuntimeException(msg);
			}
		});
	}

	private void update(CpDO cpDO) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				cpMapper.updateById(cpDO);
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
