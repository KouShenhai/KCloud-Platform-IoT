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

package org.laokou.admin.loginLog.convertor;

import org.laokou.admin.loginLog.dto.clientobject.LoginLogCO;
import org.laokou.admin.loginLog.dto.excel.LoginLogExcel;
import org.laokou.admin.loginLog.gatewayimpl.database.dataobject.LoginLogDO;
import org.laokou.admin.loginLog.model.LoginLogE;
import org.laokou.common.excel.utils.ExcelUtil;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.ObjectUtil;

import java.util.List;
import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;

/**
 * 登录日志转换器.
 *
 * @author laokou
 */
public class LoginLogConvertor implements ExcelUtil.ExcelConvert<LoginLogDO, LoginLogExcel> {

	public static final LoginLogConvertor INSTANCE = new LoginLogConvertor();

	private static final int FAIL = 1;

	public static LoginLogDO toDataObject(LoginLogE loginLogE, boolean isInsert) {
		LoginLogDO loginLogDO = new LoginLogDO();
		if (isInsert) {
			loginLogDO.generatorId();
		}
		else {
			loginLogDO.setId(loginLogE.getId());
		}
		loginLogDO.setUsername(loginLogE.getUsername());
		loginLogDO.setIp(loginLogE.getIp());
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
		LoginLogCO loginLogCO = new LoginLogCO();
		loginLogCO.setId(loginLogDO.getId());
		loginLogCO.setUsername(loginLogDO.getUsername());
		loginLogCO.setIp(loginLogDO.getIp());
		loginLogCO.setAddress(loginLogDO.getAddress());
		loginLogCO.setBrowser(loginLogDO.getBrowser());
		loginLogCO.setOs(loginLogDO.getOs());
		loginLogCO.setStatus(loginLogDO.getStatus());
		loginLogCO.setErrorMessage(loginLogDO.getErrorMessage());
		loginLogCO.setType(loginLogDO.getType());
		loginLogCO.setCreateTime(loginLogDO.getCreateTime());
		return loginLogCO;
	}

	public static LoginLogE toEntity(LoginLogCO loginLogCO) {
		LoginLogE loginLogE = new LoginLogE();
		loginLogE.setId(loginLogCO.getId());
		loginLogE.setUsername(loginLogCO.getUsername());
		loginLogE.setIp(loginLogCO.getIp());
		loginLogE.setAddress(loginLogCO.getAddress());
		loginLogE.setBrowser(loginLogCO.getBrowser());
		loginLogE.setOs(loginLogCO.getOs());
		loginLogE.setStatus(loginLogCO.getStatus());
		loginLogE.setErrorMessage(loginLogCO.getErrorMessage());
		loginLogE.setType(loginLogCO.getType());
		return loginLogE;
	}

	@Override
	public List<LoginLogExcel> toExcelList(List<LoginLogDO> list) {
		return list.stream().map(this::toExcel).toList();
	}

	private LoginLogExcel toExcel(LoginLogDO loginLogDO) {
		LoginLogExcel loginLogExcel = new LoginLogExcel();
		loginLogExcel.setUsername(loginLogDO.getUsername());
		loginLogExcel.setIp(loginLogDO.getIp());
		loginLogExcel.setAddress(loginLogDO.getAddress());
		loginLogExcel.setBrowser(loginLogDO.getBrowser());
		loginLogExcel.setOs(loginLogDO.getOs());
		loginLogExcel.setStatus(getStatusDesc(loginLogDO.getStatus()));
		loginLogExcel.setType(getTypeDesc(loginLogDO.getType()));
		loginLogExcel.setErrorMessage(loginLogDO.getErrorMessage());
		loginLogExcel.setCreateTime(DateUtil.format(loginLogDO.getCreateTime(), DateUtil.getDefaultZoneId(),
				DateUtil.YYYY_B_MM_B_DD_HH_R_MM_R_SS));
		return loginLogExcel;
	}

	private String getTypeDesc(String type) {
		if (ObjectUtil.equals(type, "password")) {
			return "用户名密码登录";
		}
		else if (ObjectUtil.equals(type, "mail")) {
			return "邮箱登录";
		}
		else if (ObjectUtil.equals(type, "mobile")) {
			return "手机号登录";
		}
		else if (ObjectUtil.equals(type, "authorization_code")) {
			return "授权码登录";
		}
		return EMPTY;
	}

	private String getStatusDesc(Integer status) {
		if (ObjectUtil.equals(status, FAIL)) {
			return "登录失败";
		}
		else {
			return "登录成功";
		}
	}

}
