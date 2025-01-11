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

package org.laokou.admin.noticeLog.convertor;

import org.laokou.admin.noticeLog.dto.clientobject.NoticeLogCO;
import org.laokou.admin.noticeLog.gatewayimpl.database.dataobject.NoticeLogDO;
import org.laokou.admin.noticeLog.model.NoticeLogE;

import java.util.List;

/**
 * 通知日志转换器.
 *
 * @author laokou
 */
public class NoticeLogConvertor {

	public static NoticeLogDO toDataObject(NoticeLogE noticeLogE, boolean isInsert) {
		NoticeLogDO noticeLogDO = new NoticeLogDO();
		if (isInsert) {
			noticeLogDO.generatorId();
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
		NoticeLogE noticeLogE = new NoticeLogE();
		noticeLogE.setId(noticeLogCO.getId());
		noticeLogE.setCode(noticeLogCO.getCode());
		noticeLogE.setName(noticeLogCO.getName());
		noticeLogE.setStatus(noticeLogCO.getStatus());
		noticeLogE.setErrorMessage(noticeLogCO.getErrorMessage());
		noticeLogE.setParam(noticeLogCO.getParam());
		return noticeLogE;
	}

}
