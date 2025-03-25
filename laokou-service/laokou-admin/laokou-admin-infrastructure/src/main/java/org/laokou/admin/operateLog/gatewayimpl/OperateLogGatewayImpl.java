/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.operateLog.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.operateLog.model.OperateLogE;
import org.laokou.common.log.mapper.OperateLogMapper;
import org.laokou.common.log.mapper.OperateLogDO;
import org.springframework.stereotype.Component;
import org.laokou.admin.operateLog.gateway.OperateLogGateway;

import java.util.Arrays;
import org.laokou.admin.operateLog.convertor.OperateLogConvertor;

/**
 * 操作日志网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class OperateLogGatewayImpl implements OperateLogGateway {

	private final OperateLogMapper operateLogMapper;

	@Override
	public void create(OperateLogE operateLogE) {
		operateLogMapper.insert(OperateLogConvertor.toDataObject(operateLogE, true));
	}

	@Override
	public void update(OperateLogE operateLogE) {
		OperateLogDO operateLogDO = OperateLogConvertor.toDataObject(operateLogE, false);
		operateLogDO.setVersion(operateLogMapper.selectVersion(operateLogE.getId()));
		operateLogMapper.updateById(operateLogDO);
	}

	@Override
	public void delete(Long[] ids) {
		operateLogMapper.deleteByIds(Arrays.asList(ids));
	}

}
