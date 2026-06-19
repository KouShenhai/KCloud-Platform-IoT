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
import org.laokou.admin.operateLog.model.AdminOperateLogE;
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

	public static OperateLogDO toDataObject(Long id, AdminOperateLogE adminOperateLogE, boolean isInsert) {
		OperateLogDO operateLogDO = new OperateLogDO();
		if (isInsert) {
			operateLogDO.setId(id);
		}
		else {
			operateLogDO.setId(adminOperateLogE.getId());
		}
		operateLogDO.setName(adminOperateLogE.getName());
		operateLogDO.setModuleName(adminOperateLogE.getModuleName());
		operateLogDO.setUri(adminOperateLogE.getUri());
		operateLogDO.setRequestType(adminOperateLogE.getRequestType());
		operateLogDO.setUserAgent(adminOperateLogE.getUserAgent());
		operateLogDO.setAddress(adminOperateLogE.getAddress());
		operateLogDO.setOperator(adminOperateLogE.getOperator());
		operateLogDO.setMethodName(adminOperateLogE.getMethodName());
		operateLogDO.setRequestParams(adminOperateLogE.getRequestParams());
		operateLogDO.setErrorMessage(adminOperateLogE.getErrorMessage());
		operateLogDO.setStatus(adminOperateLogE.getStatus());
		operateLogDO.setCostTime(adminOperateLogE.getCostTime());
		operateLogDO.setIpAddress(adminOperateLogE.getIpAddress());
		operateLogDO.setProfile(adminOperateLogE.getProfile());
		operateLogDO.setServiceAddress(adminOperateLogE.getServiceAddress());
		operateLogDO.setStackTrace(adminOperateLogE.getStackTrace());
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
		operateLogCO.setIpAddress(operateLogDO.getIpAddress());
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

	public static AdminOperateLogE toEntity(OperateLogCO operateLogCO) {
		AdminOperateLogE adminOperateLogE = OperateLogDomainFactory.getOperateLog();
		adminOperateLogE.setId(operateLogCO.getId());
		adminOperateLogE.setName(operateLogCO.getName());
		adminOperateLogE.setModuleName(operateLogCO.getModuleName());
		adminOperateLogE.setUri(operateLogCO.getUri());
		adminOperateLogE.setMethodName(operateLogCO.getMethodName());
		adminOperateLogE.setRequestType(operateLogCO.getRequestType());
		adminOperateLogE.setRequestParams(operateLogCO.getRequestParams());
		adminOperateLogE.setUserAgent(operateLogCO.getUserAgent());
		adminOperateLogE.setIpAddress(operateLogCO.getIpAddress());
		adminOperateLogE.setAddress(operateLogCO.getAddress());
		adminOperateLogE.setStatus(operateLogCO.getStatus());
		adminOperateLogE.setOperator(operateLogCO.getOperator());
		adminOperateLogE.setErrorMessage(operateLogCO.getErrorMessage());
		adminOperateLogE.setCostTime(operateLogCO.getCostTime());
		adminOperateLogE.setProfile(operateLogCO.getProfile());
		adminOperateLogE.setServiceAddress(operateLogCO.getServiceAddress());
		adminOperateLogE.setStackTrace(operateLogCO.getStackTrace());
		return adminOperateLogE;
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
		operateLogExcel.setIpAddress(operateLogDO.getIpAddress());
		operateLogExcel.setAddress(operateLogDO.getAddress());
		operateLogExcel.setStatus(status.getDesc());
		operateLogExcel.setErrorMessage(operateLogDO.getErrorMessage());
		operateLogExcel.setCostTime(operateLogDO.getCostTime());
		operateLogExcel.setCreateTime(InstantUtils.format(operateLogDO.getCreateTime(), ZoneId.of("Asia/Shanghai"),
				DateConstants.YYYY_B_MM_B_DD_HH_R_MM_R_SS));
		return operateLogExcel;
	}

}
