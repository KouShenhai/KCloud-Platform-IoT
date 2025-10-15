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

package org.laokou.admin.noticeLog.convertor;

import org.laokou.admin.noticeLog.dto.clientobject.NoticeLogCO;
import org.laokou.admin.noticeLog.dto.excel.NoticeLogExcel;
import org.laokou.admin.noticeLog.factory.NoticeLogDomainFactory;
import org.laokou.admin.noticeLog.gatewayimpl.database.dataobject.NoticeLogDO;
import org.laokou.admin.noticeLog.model.NoticeLogE;
import org.laokou.admin.noticeLog.model.Status;
import org.laokou.common.excel.util.ExcelUtils;
import org.laokou.common.i18n.common.constant.DateConstants;
import org.laokou.common.i18n.util.InstantUtils;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 通知日志转换器.
 *
 * @author laokou
 */
public final class NoticeLogConvertor implements ExcelUtils.ExcelConvertor<NoticeLogDO, NoticeLogExcel> {

	public static final NoticeLogConvertor INSTANCE = new NoticeLogConvertor();

	private NoticeLogConvertor() {

	}

	public static NoticeLogDO toDataObject(Long id, NoticeLogE noticeLogE, boolean isInsert) {
		NoticeLogDO noticeLogDO = new NoticeLogDO();
		if (isInsert) {
			noticeLogDO.setId(id);
		}
		else {
			noticeLogDO.setId(noticeLogE.getId());
		}
		noticeLogDO.setCode(noticeLogE.getCode());
		noticeLogDO.setName(noticeLogE.getName());
		noticeLogDO.setStatus(noticeLogE.getStatus());
		noticeLogDO.setErrorMessage(noticeLogE.getErrorMessage());
		noticeLogDO.setParam(noticeLogE.getParam());
		return noticeLogDO;
	}

	public static List<NoticeLogCO> toClientObjects(List<NoticeLogDO> list) {
		return list.stream().map(NoticeLogConvertor::toClientObject).toList();
	}

	public static NoticeLogCO toClientObject(NoticeLogDO noticeLogDO) {
		NoticeLogCO noticeLogCO = new NoticeLogCO();
		noticeLogCO.setId(noticeLogDO.getId());
		noticeLogCO.setCode(noticeLogDO.getCode());
		noticeLogCO.setName(noticeLogDO.getName());
		noticeLogCO.setStatus(noticeLogDO.getStatus());
		noticeLogCO.setErrorMessage(noticeLogDO.getErrorMessage());
		noticeLogCO.setParam(noticeLogDO.getParam());
		noticeLogCO.setCreateTime(noticeLogDO.getCreateTime());
		return noticeLogCO;
	}

	public static NoticeLogE toEntity(NoticeLogCO noticeLogCO) {
		NoticeLogE noticeLogE = NoticeLogDomainFactory.getNoticeLog();
		noticeLogE.setId(noticeLogCO.getId());
		noticeLogE.setCode(noticeLogCO.getCode());
		noticeLogE.setName(noticeLogCO.getName());
		noticeLogE.setStatus(noticeLogCO.getStatus());
		noticeLogE.setErrorMessage(noticeLogCO.getErrorMessage());
		noticeLogE.setParam(noticeLogCO.getParam());
		return noticeLogE;
	}

	@Override
	public List<NoticeLogExcel> toExcels(List<NoticeLogDO> list) {
		return list.stream().map(this::toExcel).toList();
	}

	@Override
	public NoticeLogDO toDataObject(NoticeLogExcel excel) {
		return null;
	}

	private NoticeLogExcel toExcel(NoticeLogDO noticeLogDO) {
		Status status = Status.getByCode(noticeLogDO.getStatus());
		Assert.notNull(status, "通知状态不存在");
		NoticeLogExcel noticeLogExcel = new NoticeLogExcel();
		noticeLogExcel.setCode(noticeLogDO.getCode());
		noticeLogExcel.setName(noticeLogDO.getName());
		noticeLogExcel.setStatus(status.getDesc());
		noticeLogExcel.setParam(noticeLogDO.getParam());
		noticeLogExcel.setErrorMessage(noticeLogDO.getErrorMessage());
		noticeLogExcel
			.setCreateTime(InstantUtils.format(noticeLogDO.getCreateTime(), DateConstants.YYYY_B_MM_B_DD_HH_R_MM_R_SS));
		return noticeLogExcel;
	}

}
