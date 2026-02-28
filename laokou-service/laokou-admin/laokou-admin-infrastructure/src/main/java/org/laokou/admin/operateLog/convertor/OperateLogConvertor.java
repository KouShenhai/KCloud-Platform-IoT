/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.operateLog.convertor;

import org.laokou.admin.noticeLog.model.Status;
import org.laokou.admin.operateLog.dto.clientobject.OperateLogCO;
import org.laokou.admin.operateLog.dto.excel.OperateLogExcel;
import org.laokou.admin.operateLog.factory.OperateLogDomainFactory;
import org.laokou.admin.operateLog.model.OperateLog111E;
import org.laokou.common.excel.util.ExcelUtils;
import org.laokou.common.i18n.common.constant.DateConstants;
import org.laokou.common.i18n.util.InstantUtils;
import org.laokou.common.log.mapper.OperateLogDO;
import org.springframework.util.Assert;

import java.time.ZoneId;
import java.util.List;

/**
 * 操作日志转换器.
 *
 * @author laokou
 */
public final class OperateLogConvertor implements ExcelUtils.ExcelConvertor<OperateLogDO, OperateLogExcel> {

	public static final OperateLogConvertor INSTANCE = new OperateLogConvertor();

	private OperateLogConvertor() {
	}

	public static OperateLogDO toDataObject(Long id, OperateLog111E operateLog111E, boolean isInsert) {
		OperateLogDO operateLogDO = new OperateLogDO();
		if (isInsert) {
			operateLogDO.setId(id);
		}
		else {
			operateLogDO.setId(operateLog111E.getId());
		}
		operateLogDO.setName(operateLog111E.getName());
		operateLogDO.setModuleName(operateLog111E.getModuleName());
		operateLogDO.setUri(operateLog111E.getUri());
		operateLogDO.setRequestType(operateLog111E.getRequestType());
		operateLogDO.setUserAgent(operateLog111E.getUserAgent());
		operateLogDO.setAddress(operateLog111E.getAddress());
		operateLogDO.setOperator(operateLog111E.getOperator());
		operateLogDO.setMethodName(operateLog111E.getMethodName());
		operateLogDO.setRequestParams(operateLog111E.getRequestParams());
		operateLogDO.setErrorMessage(operateLog111E.getErrorMessage());
		operateLogDO.setStatus(operateLog111E.getStatus());
		operateLogDO.setCostTime(operateLog111E.getCostTime());
		operateLogDO.setIp(operateLog111E.getIp());
		operateLogDO.setProfile(operateLog111E.getProfile());
		operateLogDO.setServiceAddress(operateLog111E.getServiceAddress());
		operateLogDO.setStackTrace(operateLog111E.getStackTrace());
		return operateLogDO;
	}

	public static OperateLogCO toClientObject(OperateLogDO operateLogDO) {
		OperateLogCO operateLogCO = new OperateLogCO();
		operateLogCO.setId(operateLogDO.getId());
		operateLogCO.setName(operateLogDO.getName());
		operateLogCO.setModuleName(operateLogDO.getModuleName());
		operateLogCO.setUri(operateLogDO.getUri());
		operateLogCO.setMethodName(operateLogDO.getMethodName());
		operateLogCO.setRequestType(operateLogDO.getRequestType());
		operateLogCO.setRequestParams(operateLogDO.getRequestParams());
		operateLogCO.setUserAgent(operateLogDO.getUserAgent());
		operateLogCO.setIp(operateLogDO.getIp());
		operateLogCO.setAddress(operateLogDO.getAddress());
		operateLogCO.setStatus(operateLogDO.getStatus());
		operateLogCO.setOperator(operateLogDO.getOperator());
		operateLogCO.setErrorMessage(operateLogDO.getErrorMessage());
		operateLogCO.setCostTime(operateLogDO.getCostTime());
		operateLogCO.setProfile(operateLogDO.getProfile());
		operateLogCO.setServiceAddress(operateLogDO.getServiceAddress());
		operateLogCO.setStackTrace(operateLogDO.getStackTrace());
		operateLogCO.setCreateTime(operateLogDO.getCreateTime());
		operateLogCO.setServiceId(operateLogDO.getServiceId());
		return operateLogCO;
	}

	public static List<OperateLogCO> toClientObjects(List<OperateLogDO> list) {
		return list.stream().map(OperateLogConvertor::toClientObject).toList();
	}

	public static OperateLog111E toEntity(OperateLogCO operateLogCO) {
		OperateLog111E operateLog111E = OperateLogDomainFactory.getOperateLog();
		operateLog111E.setId(operateLogCO.getId());
		operateLog111E.setName(operateLogCO.getName());
		operateLog111E.setModuleName(operateLogCO.getModuleName());
		operateLog111E.setUri(operateLogCO.getUri());
		operateLog111E.setMethodName(operateLogCO.getMethodName());
		operateLog111E.setRequestType(operateLogCO.getRequestType());
		operateLog111E.setRequestParams(operateLogCO.getRequestParams());
		operateLog111E.setUserAgent(operateLogCO.getUserAgent());
		operateLog111E.setIp(operateLogCO.getIp());
		operateLog111E.setAddress(operateLogCO.getAddress());
		operateLog111E.setStatus(operateLogCO.getStatus());
		operateLog111E.setOperator(operateLogCO.getOperator());
		operateLog111E.setErrorMessage(operateLogCO.getErrorMessage());
		operateLog111E.setCostTime(operateLogCO.getCostTime());
		operateLog111E.setProfile(operateLogCO.getProfile());
		operateLog111E.setServiceAddress(operateLogCO.getServiceAddress());
		operateLog111E.setStackTrace(operateLogCO.getStackTrace());
		return operateLog111E;
	}

	@Override
	public List<OperateLogExcel> toExcels(List<OperateLogDO> list) {
		return list.stream().map(this::toExcel).toList();
	}

	@Override
	public OperateLogDO toDataObject(OperateLogExcel operateLogExcel) {
		return null;
	}

	private OperateLogExcel toExcel(OperateLogDO operateLogDO) {
		Status status = Status.getByCode(operateLogDO.getStatus());
		Assert.notNull(status, "操作状态不存在");
		OperateLogExcel operateLogExcel = new OperateLogExcel();
		operateLogExcel.setName(operateLogDO.getName());
		operateLogExcel.setModuleName(operateLogDO.getModuleName());
		operateLogExcel.setRequestType(operateLogDO.getRequestType());
		operateLogExcel.setOperator(operateLogDO.getOperator());
		operateLogExcel.setIp(operateLogDO.getIp());
		operateLogExcel.setAddress(operateLogDO.getAddress());
		operateLogExcel.setStatus(status.getDesc());
		operateLogExcel.setErrorMessage(operateLogDO.getErrorMessage());
		operateLogExcel.setCostTime(operateLogDO.getCostTime());
		operateLogExcel.setCreateTime(InstantUtils.format(operateLogDO.getCreateTime(), ZoneId.of("Asia/Shanghai"),
				DateConstants.YYYY_B_MM_B_DD_HH_R_MM_R_SS));
		return operateLogExcel;
	}

}
