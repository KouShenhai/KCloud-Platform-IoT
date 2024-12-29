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

import org.laokou.auth.dto.clientobject.NoticeLogCO;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.auth.gatewayimpl.database.dataobject.NoticeLogDO;
import org.laokou.auth.model.NoticeLogE;
import org.laokou.common.i18n.dto.DomainEvent;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.mail.dto.MailResult;

/**
 * @author laokou
 */
public class NoticeLogConvertor {

	public static NoticeLogCO toClientObject(DomainEvent domainEvent, MailResult result, String uuid) {
		NoticeLogCO co = new NoticeLogCO();
  co.setCode(result.getCode());
  co.setName(result.getName());
  co.setStatus(result.getStatus());
  co.setErrorMessage(StringUtil.isNotEmpty(result.getErrorMessage()) ? result.getErrorMessage().substring(0,2000) : null);
  co.setParam(result.getParam());
  co.setTenantId(domainEvent.getTenantId());
		co.setId(domainEvent.getAggregateId());
		co.setInstant(domainEvent.getInstant());
		co.setUuid(uuid);
		co.setCaptcha(result.getCaptcha());
		return co;
	}

	public static NoticeLogE toEntity(NoticeLogCO noticeLogCO) {
		NoticeLogE noticeLogE = DomainFactory.getNoticeLog();
		noticeLogE.setId(noticeLogCO.getId());
		noticeLogE.setCode(noticeLogCO.getCode());
		noticeLogE.setName(noticeLogCO.getName());
		noticeLogE.setStatus(noticeLogCO.getStatus());
		noticeLogE.setErrorMessage(noticeLogCO.getErrorMessage());
		noticeLogE.setParam(noticeLogCO.getParam());
		noticeLogE.setTenantId(noticeLogCO.getTenantId());
		noticeLogE.setInstant(noticeLogCO.getInstant());
		noticeLogE.setUuid(noticeLogCO.getUuid());
		noticeLogE.setCaptcha(noticeLogCO.getCaptcha());
		return noticeLogE;
	}

	public static NoticeLogDO toDataObject(NoticeLogE noticeLogE) {
		NoticeLogDO noticeLogDO = new NoticeLogDO();
		noticeLogDO.setId(noticeLogE.getId());
		noticeLogDO.setCode(noticeLogE.getCode());
		noticeLogDO.setName(noticeLogE.getName());
		noticeLogDO.setStatus(noticeLogE.getStatus());
		noticeLogDO.setErrorMessage(noticeLogE.getErrorMessage());
		noticeLogDO.setParam(noticeLogE.getParam());
		noticeLogDO.setTenantId(noticeLogE.getTenantId());
		noticeLogDO.setCreateTime(noticeLogE.getInstant());
		noticeLogDO.setUpdateTime(noticeLogE.getInstant());
		return noticeLogDO;
	}

}
