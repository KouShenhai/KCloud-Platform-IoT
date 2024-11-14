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

package org.laokou.auth.convertor;

import org.laokou.auth.dto.domainevent.NoticeMessageEvent;
import org.laokou.auth.gatewayimpl.database.dataobject.NoticeLogDO;

/**
 * @author laokou
 */
public class NoticeLogConvertor {

	public static NoticeLogDO toDataObject(NoticeMessageEvent noticeMessageEvent) {
		NoticeLogDO noticeLogDO = new NoticeLogDO();
		noticeLogDO.setId(noticeMessageEvent.getId());
		noticeLogDO.setCreator(noticeMessageEvent.getCreator());
		noticeLogDO.setEditor(noticeMessageEvent.getEditor());
		noticeLogDO.setCreateTime(noticeMessageEvent.getCreateTime());
		noticeLogDO.setUpdateTime(noticeMessageEvent.getUpdateTime());
		noticeLogDO.setTenantId(noticeMessageEvent.getTenantId());
		noticeLogDO.setCode(noticeMessageEvent.getCode());
		noticeLogDO.setName(noticeMessageEvent.getName());
		noticeLogDO.setStatus(noticeMessageEvent.getStatus());
		noticeLogDO.setErrorMessage(noticeMessageEvent.getErrorMessage());
		noticeLogDO.setParam(noticeMessageEvent.getParam());
		return noticeLogDO;
	}

}
