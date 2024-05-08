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

import lombok.RequiredArgsConstructor;
import org.laokou.admin.domain.gateway.LogGateway;
import org.laokou.admin.dto.domainevent.FileUploadEvent;
import org.laokou.admin.gatewayimpl.database.OperateLogMapper;
import org.laokou.admin.gatewayimpl.database.OssLogMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.OssLogDO;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.dto.DefaultDomainEvent;
import org.springframework.stereotype.Component;

/**
 * 日志管理.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class LogGatewayImpl implements LogGateway {

	private final OperateLogMapper operateLogMapper;

	private final OssLogMapper ossLogMapper;

//	@Override
//	public void create(OperateEvent event, DefaultDomainEvent evt) {
//		operateLogMapper.insert(null);
//	}
//
//	@Override
//	public void create(FileUploadEvent event, DefaultDomainEvent evt) {
//		ossLogMapper.insert(convert(event, evt));
//	}

	private OssLogDO convert(FileUploadEvent fileUploadEvent, DefaultDomainEvent evt) {
		OssLogDO logDO = new OssLogDO();
		logDO.setMd5(fileUploadEvent.getMd5());
		logDO.setUrl(fileUploadEvent.getUrl());
		logDO.setName(fileUploadEvent.getName());
		logDO.setSize(fileUploadEvent.getSize());
		logDO.setStatus(fileUploadEvent.getStatus());
		logDO.setErrorMessage(fileUploadEvent.getErrorMessage());
		logDO.setId(IdGenerator.defaultSnowflakeId());
		logDO.setEditor(evt.getEditor());
		logDO.setCreator(evt.getCreator());
		logDO.setCreateDate(evt.getCreateDate());
		logDO.setUpdateDate(evt.getUpdateDate());
		logDO.setDeptId(evt.getDeptId());
		logDO.setDeptPath(evt.getDeptPath());
		logDO.setTenantId(evt.getTenantId());
		logDO.setEventId(evt.getId());
		return logDO;
	}

	// private OperateLogDO convert(OperateEvent operateEvent, DecorateDomainEvent evt) {
	// OperateLogDO logDO = new OperateLogDO();
	// logDO.setName(operateEvent.getName());
	// logDO.setModuleName(operateEvent.getModuleName());
	// logDO.setUri(operateEvent.getUri());
	// logDO.setMethodName(operateEvent.getMethodName());
	// logDO.setRequestType(operateEvent.getRequestType());
	// logDO.setRequestParams(operateEvent.getRequestParams());
	// logDO.setUserAgent(operateEvent.getUserAgent());
	// logDO.setIp(operateEvent.getIp());
	// logDO.setStatus(operateEvent.getStatus());
	// logDO.setOperator(operateEvent.getOperator());
	// logDO.setErrorMessage(operateEvent.getErrorMessage());
	// logDO.setTakeTime(operateEvent.getTakeTime());
	// logDO.setAddress(operateEvent.getAddress());
	// logDO.setId(IdGenerator.defaultSnowflakeId());
	// logDO.setEditor(evt.getEditor());
	// logDO.setCreator(evt.getCreator());
	// logDO.setCreateDate(evt.getCreateDate());
	// logDO.setUpdateDate(evt.getUpdateDate());
	// logDO.setDeptId(evt.getDeptId());
	// logDO.setDeptPath(evt.getDeptPath());
	// logDO.setTenantId(evt.getTenantId());
	// logDO.setEventId(evt.getId());
	// return logDO;
	// }

}
