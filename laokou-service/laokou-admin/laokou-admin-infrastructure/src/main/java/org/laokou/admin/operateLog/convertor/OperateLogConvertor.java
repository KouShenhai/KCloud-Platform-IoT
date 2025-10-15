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

package org.laokou.admin.operateLog.convertor;

import org.laokou.admin.noticeLog.model.Status;
import org.laokou.admin.operateLog.dto.clientobject.OperateLogCO;
import org.laokou.admin.operateLog.dto.excel.OperateLogExcel;
import org.laokou.admin.operateLog.factory.OperateLogDomainFactory;
import org.laokou.admin.operateLog.model.OperateLogE;
import org.laokou.common.excel.util.ExcelUtils;
import org.laokou.common.i18n.common.constant.DateConstants;
import org.laokou.common.i18n.util.InstantUtils;
import org.laokou.common.log.mapper.OperateLogDO;
import org.springframework.util.Assert;

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

	public static OperateLogDO toDataObject(Long id, OperateLogE operateLogE, boolean isInsert) {
		OperateLogDO operateLogDO = new OperateLogDO();
		if (isInsert) {
			operateLogDO.setId(id);
		}
		else {
			operateLogDO.setId(operateLogE.getId());
		}
		operateLogDO.setName(operateLogE.getName());
		operateLogDO.setModuleName(operateLogE.getModuleName());
		operateLogDO.setUri(operateLogE.getUri());
		operateLogDO.setRequestType(operateLogE.getRequestType());
		operateLogDO.setUserAgent(operateLogE.getUserAgent());
		operateLogDO.setAddress(operateLogE.getAddress());
		operateLogDO.setOperator(operateLogE.getOperator());
		operateLogDO.setMethodName(operateLogE.getMethodName());
		operateLogDO.setRequestParams(operateLogE.getRequestParams());
		operateLogDO.setErrorMessage(operateLogE.getErrorMessage());
		operateLogDO.setStatus(operateLogE.getStatus());
		operateLogDO.setCostTime(operateLogE.getCostTime());
		operateLogDO.setIp(operateLogE.getIp());
		operateLogDO.setProfile(operateLogE.getProfile());
		operateLogDO.setServiceAddress(operateLogE.getServiceAddress());
		operateLogDO.setStackTrace(operateLogE.getStackTrace());
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

	public static OperateLogE toEntity(OperateLogCO operateLogCO) {
		OperateLogE operateLogE = OperateLogDomainFactory.getOperateLog();
		operateLogE.setId(operateLogCO.getId());
		operateLogE.setName(operateLogCO.getName());
		operateLogE.setModuleName(operateLogCO.getModuleName());
		operateLogE.setUri(operateLogCO.getUri());
		operateLogE.setMethodName(operateLogCO.getMethodName());
		operateLogE.setRequestType(operateLogCO.getRequestType());
		operateLogE.setRequestParams(operateLogCO.getRequestParams());
		operateLogE.setUserAgent(operateLogCO.getUserAgent());
		operateLogE.setIp(operateLogCO.getIp());
		operateLogE.setAddress(operateLogCO.getAddress());
		operateLogE.setStatus(operateLogCO.getStatus());
		operateLogE.setOperator(operateLogCO.getOperator());
		operateLogE.setErrorMessage(operateLogCO.getErrorMessage());
		operateLogE.setCostTime(operateLogCO.getCostTime());
		operateLogE.setProfile(operateLogCO.getProfile());
		operateLogE.setServiceAddress(operateLogCO.getServiceAddress());
		operateLogE.setStackTrace(operateLogCO.getStackTrace());
		return operateLogE;
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
		operateLogExcel.setCreateTime(
				InstantUtils.format(operateLogDO.getCreateTime(), DateConstants.YYYY_B_MM_B_DD_HH_R_MM_R_SS));
		return operateLogExcel;
	}

}
