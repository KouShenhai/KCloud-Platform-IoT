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

package org.laokou.auth.convertor;

import org.laokou.auth.dto.clientobject.NoticeLogCO;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.auth.gatewayimpl.database.dataobject.NoticeLogDO;
import org.laokou.auth.model.NoticeLogE;
import org.laokou.common.i18n.dto.DomainEvent;
import org.laokou.common.mail.dto.MailResult;
import org.laokou.common.sms.entity.SmsResult;

import static org.laokou.common.i18n.util.StringUtils.truncate;

/**
 * @author laokou
 */
public final class NoticeLogConvertor {

	private NoticeLogConvertor() {
	}

	public static NoticeLogCO toClientObject(DomainEvent domainEvent, MailResult result, String uuid) {
		NoticeLogCO noticeLogCO = new NoticeLogCO();
		noticeLogCO.setCode(result.getCode());
		noticeLogCO.setName(result.getName());
		noticeLogCO.setStatus(result.getStatus());
		noticeLogCO.setErrorMessage(truncate(result.getErrorMessage(), 2000));
		noticeLogCO.setParam(result.getParam());
		noticeLogCO.setTenantId(domainEvent.getTenantId());
		noticeLogCO.setId(domainEvent.getAggregateId());
		noticeLogCO.setInstant(domainEvent.getInstant());
		noticeLogCO.setUuid(uuid);
		noticeLogCO.setCaptcha(result.getCaptcha());
		return noticeLogCO;
	}

	public static NoticeLogCO toClientObject(DomainEvent domainEvent, SmsResult result, String uuid) {
		NoticeLogCO noticeLogCO = new NoticeLogCO();
		noticeLogCO.setCode(result.getCode());
		noticeLogCO.setName(result.getName());
		noticeLogCO.setStatus(result.getStatus());
		noticeLogCO.setErrorMessage(truncate(result.getErrorMessage(), 2000));
		noticeLogCO.setParam(result.getParam());
		noticeLogCO.setTenantId(domainEvent.getTenantId());
		noticeLogCO.setId(domainEvent.getAggregateId());
		noticeLogCO.setInstant(domainEvent.getInstant());
		noticeLogCO.setUuid(uuid);
		noticeLogCO.setCaptcha(result.getCaptcha());
		return noticeLogCO;
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
