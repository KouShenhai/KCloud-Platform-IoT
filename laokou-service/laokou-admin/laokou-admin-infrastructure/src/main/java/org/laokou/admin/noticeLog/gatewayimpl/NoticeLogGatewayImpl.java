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

package org.laokou.admin.noticeLog.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.noticeLog.convertor.NoticeLogConvertor;
import org.laokou.admin.noticeLog.gateway.NoticeLogGateway;
import org.laokou.admin.noticeLog.gatewayimpl.database.NoticeLogMapper;
import org.laokou.admin.noticeLog.gatewayimpl.database.dataobject.NoticeLogDO;
import org.laokou.admin.noticeLog.model.NoticeLogE;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 通知日志网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class NoticeLogGatewayImpl implements NoticeLogGateway {

	private final NoticeLogMapper noticeLogMapper;

	@Override
	public void create(NoticeLogE noticeLogE) {
		noticeLogMapper.insert(NoticeLogConvertor.toDataObject(noticeLogE, true));
	}

	@Override
	public void update(NoticeLogE noticeLogE) {
		NoticeLogDO noticeLogDO = NoticeLogConvertor.toDataObject(noticeLogE, false);
		noticeLogDO.setVersion(noticeLogMapper.selectVersion(noticeLogE.getId()));
		noticeLogMapper.updateById(noticeLogDO);
	}

	@Override
	public void delete(Long[] ids) {
		noticeLogMapper.deleteByIds(Arrays.asList(ids));
	}

}
