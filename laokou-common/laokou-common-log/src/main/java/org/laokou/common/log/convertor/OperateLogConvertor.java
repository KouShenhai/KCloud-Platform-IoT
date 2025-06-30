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

package org.laokou.common.log.convertor;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.laokou.common.i18n.util.StringUtils;
import org.laokou.common.log.factory.DomainFactory;
import org.laokou.common.log.mapper.OperateLogDO;
import org.laokou.common.log.handler.event.OperateEvent;
import org.laokou.common.log.model.OperateLogA;
import org.laokou.common.mybatisplus.util.UserUtils;

public final class OperateLogConvertor {

	private OperateLogConvertor() {
	}

	public static OperateLogA toEntity() {
		return DomainFactory.getOperateLog();
	}

	public static OperateEvent toDomainEvent(OperateLogA operateLogA) {
		return new OperateEvent(operateLogA.getId(), operateLogA.getName(), operateLogA.getModuleName(),
				operateLogA.getUri(), operateLogA.getMethodName(), operateLogA.getRequestType(),
				operateLogA.getRequestParams(), operateLogA.getUserAgent(), operateLogA.getIp(),
				operateLogA.getAddress(), operateLogA.getStatus(), UserUtils.getUserName(),
				operateLogA.getErrorMessage(), operateLogA.getCostTime(), operateLogA.getServiceId(),
				operateLogA.getServiceAddress(), operateLogA.getProfile(), operateLogA.getStackTrace(),
				operateLogA.getCreateTime(), UserUtils.getTenantId(), UserUtils.getUserId());
	}

	public static OperateLogDO toDataObject(OperateEvent operateEvent) throws JsonProcessingException {
		OperateLogDO operateLogDO = new OperateLogDO();
		operateLogDO.setName(operateEvent.getName());
		operateLogDO.setModuleName(operateEvent.getModuleName());
		operateLogDO.setUri(operateEvent.getUri());
		operateLogDO.setRequestType(operateEvent.getRequestType());
		operateLogDO.setUserAgent(operateEvent.getUserAgent());
		operateLogDO.setAddress(operateEvent.getAddress());
		operateLogDO.setOperator(operateEvent.getOperator());
		operateLogDO.setServiceId(operateEvent.getServiceId());
		operateLogDO.setServiceAddress(operateEvent.getServiceAddress());
		operateLogDO.setStackTrace(operateEvent.getStackTrace());
		operateLogDO.setIp(operateEvent.getIp());
		operateLogDO.setProfile(operateEvent.getProfile());
		operateLogDO.setMethodName(operateEvent.getMethodName());
		operateLogDO.setRequestParams(operateEvent.getRequestParams());
		operateLogDO.setStatus(operateEvent.getStatus());
		operateLogDO.setCostTime(operateEvent.getCostTime());
		operateLogDO.setErrorMessage(truncate(operateEvent.getErrorMessage()));
		operateLogDO.setId(operateEvent.getId());
		operateLogDO.setTenantId(operateEvent.getTenantId());
		operateLogDO.setCreator(operateEvent.getUserId());
		operateLogDO.setEditor(operateEvent.getUserId());
		operateLogDO.setCreateTime(operateEvent.getCreateTime());
		operateLogDO.setUpdateTime(operateEvent.getCreateTime());
		return operateLogDO;
	}

	private static String truncate(String str) {
		if (!StringUtils.isNotEmpty(str)) {
			return null;
		}
		return str.length() > 2000 ? str.substring(0, 2000) : str;
	}

}
