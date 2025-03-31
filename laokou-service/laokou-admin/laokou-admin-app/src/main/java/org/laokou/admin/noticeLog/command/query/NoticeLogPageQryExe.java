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

package org.laokou.admin.noticeLog.command.query;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.noticeLog.convertor.NoticeLogConvertor;
import org.laokou.admin.noticeLog.dto.NoticeLogPageQry;
import org.laokou.admin.noticeLog.dto.clientobject.NoticeLogCO;
import org.laokou.admin.noticeLog.gatewayimpl.database.NoticeLogMapper;
import org.laokou.admin.noticeLog.gatewayimpl.database.dataobject.NoticeLogDO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.common.tenant.constant.DSConstants.DOMAIN;

/**
 * 分页查询通知日志请求执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class NoticeLogPageQryExe {

	private final NoticeLogMapper noticeLogMapper;

	public Result<Page<NoticeLogCO>> execute(NoticeLogPageQry qry) {
		try {
			DynamicDataSourceContextHolder.push(DOMAIN);
			List<NoticeLogDO> list = noticeLogMapper.selectObjectPage(qry);
			long total = noticeLogMapper.selectObjectCount(qry);
			return Result.ok(Page.create(NoticeLogConvertor.toClientObjects(list), total));
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

}
