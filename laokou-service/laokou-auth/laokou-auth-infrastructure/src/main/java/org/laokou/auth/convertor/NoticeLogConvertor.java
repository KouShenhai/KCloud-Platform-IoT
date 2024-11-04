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

import org.laokou.auth.dto.domainevent.CallApiEvent;
import org.laokou.auth.gatewayimpl.database.dataobject.NoticeLogDO;

/**
 * @author laokou
 */
public class NoticeLogConvertor {

	public static NoticeLogDO toDataObject(CallApiEvent callApiEvent) {
		NoticeLogDO noticeLogDO = new NoticeLogDO();
		noticeLogDO.setId(callApiEvent.getId());
		noticeLogDO.setCreator(callApiEvent.getCreator());
		noticeLogDO.setEditor(callApiEvent.getEditor());
		noticeLogDO.setCreateTime(callApiEvent.getCreateTime());
		noticeLogDO.setUpdateTime(callApiEvent.getUpdateTime());
		noticeLogDO.setTenantId(callApiEvent.getTenantId());
		noticeLogDO.setCode(callApiEvent.getCode());
		noticeLogDO.setName(callApiEvent.getName());
		noticeLogDO.setStatus(callApiEvent.getStatus());
		noticeLogDO.setErrorMessage(callApiEvent.getErrorMessage());
		noticeLogDO.setParam(callApiEvent.getParam());
		return noticeLogDO;
	}

}
