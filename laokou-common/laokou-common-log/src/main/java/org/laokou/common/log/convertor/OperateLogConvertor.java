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
import org.laokou.common.core.context.UserContextHolder;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.dto.DomainEvent;
import org.laokou.common.i18n.utils.JacksonUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.log.database.dataobject.OperateLogDO;
import org.laokou.common.log.event.OperateEvent;
import org.laokou.common.log.model.OperateLogE;

import static org.laokou.common.i18n.common.constant.EventType.OPERATE_EVENT;
import static org.laokou.common.log.constant.Constant.LAOKOU_LOG_TOPIC;
import static org.laokou.common.log.constant.Constant.OPERATE_TAG;

public final class OperateLogConvertor {

	private OperateLogConvertor() {
	}

	public static OperateLogDO toDataObject(DomainEvent event) throws JsonProcessingException {
		OperateEvent operateEvent = JacksonUtil.toBean(event.getPayload(), OperateEvent.class);
		OperateLogDO operateLogDO = new OperateLogDO();
		operateLogDO.setName(operateEvent.name());
		operateLogDO.setModuleName(operateEvent.moduleName());
		operateLogDO.setUri(operateEvent.uri());
		operateLogDO.setRequestType(operateEvent.requestType());
		operateLogDO.setUserAgent(operateEvent.userAgent());
		operateLogDO.setAddress(operateEvent.address());
		operateLogDO.setOperator(operateEvent.operator());
		operateLogDO.setServiceId(operateEvent.serviceId());
		operateLogDO.setServiceAddress(operateEvent.serviceAddress());
		operateLogDO.setStackTrace(operateEvent.stackTrace());
		operateLogDO.setIp(operateEvent.ip());
		operateLogDO.setProfile(operateEvent.profile());
		operateLogDO.setMethodName(operateEvent.methodName());
		operateLogDO.setRequestParams(operateEvent.requestParams());
		operateLogDO.setStatus(operateEvent.status());
		operateLogDO.setCostTime(operateEvent.costTime());
		operateLogDO.setErrorMessage(truncate(operateEvent.errorMessage()));
		operateLogDO.setId(event.getId());
		operateLogDO.setTenantId(event.getTenantId());
		operateLogDO.setCreator(event.getUserId());
		operateLogDO.setEditor(event.getUserId());
		operateLogDO.setVersion(event.getVersion());
		return operateLogDO;
	}

	public static OperateEvent toEvent(OperateLogE operateLogE) {
		return new OperateEvent(operateLogE.getName(), operateLogE.getModuleName(), operateLogE.getUri(),
				operateLogE.getMethodName(), operateLogE.getRequestType(), operateLogE.getRequestParams(),
				operateLogE.getUserAgent(), operateLogE.getIp(), operateLogE.getAddress(), operateLogE.getStatus(),
				operateLogE.getOperator(), operateLogE.getErrorMessage(), operateLogE.getCostTime(),
				operateLogE.getServiceId(), operateLogE.getServiceAddress(), operateLogE.getProfile(),
				operateLogE.getStackTrace());
	}

	public static DomainEvent toDomainEvent(OperateLogE operateLogE) {
		UserContextHolder.User user = UserContextHolder.get();
		return new DomainEvent(IdGenerator.defaultSnowflakeId(), user.getTenantId(), user.getId(), null,
				LAOKOU_LOG_TOPIC, OPERATE_TAG, 0, JacksonUtil.toJsonStr(toEvent(operateLogE)), OPERATE_EVENT,
				user.getSourcePrefix());
	}

	private static String truncate(String str) {
		if (!StringUtil.isNotEmpty(str)) {
			return null;
		}
		return str.length() > 2000 ? str.substring(0, 2000) : str;
	}

}
