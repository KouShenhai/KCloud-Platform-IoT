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

package org.laokou.admin.loginLog.convertor;

import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.loginLog.dto.clientobject.LoginLogCO;
import org.laokou.admin.loginLog.dto.excel.LoginLogExcel;
import org.laokou.admin.loginLog.factory.LoginLogDomainFactory;
import org.laokou.admin.loginLog.gatewayimpl.database.dataobject.LoginLogDO;
import org.laokou.admin.loginLog.model.LoginLogE;
import org.laokou.admin.loginLog.model.LoginStatus;
import org.laokou.admin.loginLog.model.LoginType;
import org.laokou.common.crypto.util.AESUtils;
import org.laokou.common.excel.util.ExcelUtils;
import org.laokou.common.i18n.common.constant.DateConstants;
import org.laokou.common.i18n.util.InstantUtils;
import org.springframework.util.Assert;

import java.time.ZoneId;
import java.util.List;

/**
 * 登录日志转换器.
 *
 * @author laokou
 */
@Slf4j
public final class LoginLogConvertor implements ExcelUtils.ExcelConvertor<LoginLogDO, LoginLogExcel> {

	public static final LoginLogConvertor INSTANCE = new LoginLogConvertor();

	private LoginLogConvertor() {
	}

	public static LoginLogDO toDataObject(Long id, LoginLogE loginLogE, boolean isInsert) {
		LoginLogDO loginLogDO = new LoginLogDO();
		if (isInsert) {
			loginLogDO.setId(id);
		}
		else {
			loginLogDO.setId(loginLogE.getId());
		}
		loginLogDO.setUsername(loginLogE.getUsername());
		loginLogDO.setIpAddress(loginLogE.getIpAddress());
		loginLogDO.setAddress(loginLogE.getAddress());
		loginLogDO.setBrowser(loginLogE.getBrowser());
		loginLogDO.setOs(loginLogE.getOs());
		loginLogDO.setStatus(loginLogE.getStatus());
		loginLogDO.setErrorMessage(loginLogE.getErrorMessage());
		loginLogDO.setType(loginLogE.getType());
		return loginLogDO;
	}

	public static List<LoginLogCO> toClientObjects(List<LoginLogDO> list) {
		return list.stream().map(LoginLogConvertor::toClientObject).toList();
	}

	public static LoginLogCO toClientObject(LoginLogDO loginLogDO) {
		try {
			LoginLogCO loginLogCO = new LoginLogCO();
			loginLogCO.setId(loginLogDO.getId());
			loginLogCO.setUsername(AESUtils.decrypt(loginLogDO.getUsername()));
			loginLogCO.setIpAddress(loginLogDO.getIpAddress());
			loginLogCO.setAddress(loginLogDO.getAddress());
			loginLogCO.setBrowser(loginLogDO.getBrowser());
			loginLogCO.setOs(loginLogDO.getOs());
			loginLogCO.setStatus(loginLogDO.getStatus());
			loginLogCO.setErrorMessage(loginLogDO.getErrorMessage());
			loginLogCO.setType(loginLogDO.getType());
			loginLogCO.setCreateTime(loginLogDO.getCreateTime());
			return loginLogCO;
		}
		catch (Exception ex) {
			log.error("登录日志用户名解密失败，错误信息：{}", ex.getMessage(), ex);
			throw new IllegalArgumentException(ex);
		}
	}

	public static LoginLogE toEntity(LoginLogCO loginLogCO) {
		LoginLogE loginLogE = LoginLogDomainFactory.getLoginLog();
		loginLogE.setId(loginLogCO.getId());
		loginLogE.setUsername(loginLogCO.getUsername());
		loginLogE.setIpAddress(loginLogCO.getIpAddress());
		loginLogE.setAddress(loginLogCO.getAddress());
		loginLogE.setBrowser(loginLogCO.getBrowser());
		loginLogE.setOs(loginLogCO.getOs());
		loginLogE.setStatus(loginLogCO.getStatus());
		loginLogE.setErrorMessage(loginLogCO.getErrorMessage());
		loginLogE.setType(loginLogCO.getType());
		return loginLogE;
	}

	@Override
	public List<LoginLogExcel> toExcels(List<LoginLogDO> list) {
		return list.stream().map(this::toExcel).toList();
	}

	@Override
	public LoginLogDO toDataObject(LoginLogExcel excel) {
		return null;
	}

	private LoginLogExcel toExcel(LoginLogDO loginLogDO) {
		try {
			LoginType type = LoginType.getByCode(loginLogDO.getType());
			LoginStatus status = LoginStatus.getByCode(loginLogDO.getStatus());
			Assert.notNull(type, "登录类型不存在");
			Assert.notNull(status, "登录状态不存在");
			LoginLogExcel loginLogExcel = new LoginLogExcel();
			loginLogExcel.setUsername(AESUtils.decrypt(loginLogDO.getUsername()));
			loginLogExcel.setIpAddress(loginLogDO.getIpAddress());
			loginLogExcel.setAddress(loginLogDO.getAddress());
			loginLogExcel.setBrowser(loginLogDO.getBrowser());
			loginLogExcel.setOs(loginLogDO.getOs());
			loginLogExcel.setStatus(status.getDesc());
			loginLogExcel.setType(type.getDesc());
			loginLogExcel.setErrorMessage(loginLogDO.getErrorMessage());
			loginLogExcel.setCreateTime(InstantUtils.format(loginLogDO.getCreateTime(), ZoneId.of("Asia/Shanghai"),
					DateConstants.YYYY_B_MM_B_DD_HH_R_MM_R_SS));
			return loginLogExcel;
		}
		catch (Exception ex) {
			log.error("登录日志用户名解密失败，错误信息：{}", ex.getMessage(), ex);
			throw new IllegalArgumentException(ex);
		}
	}

}
