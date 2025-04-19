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

package org.laokou.admin.noticeLog.command;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.noticeLog.convertor.NoticeLogConvertor;
import org.laokou.admin.noticeLog.dto.NoticeLogExportCmd;
import org.laokou.admin.noticeLog.dto.excel.NoticeLogExcel;
import org.laokou.admin.noticeLog.gatewayimpl.database.NoticeLogMapper;
import org.laokou.common.core.util.ResponseUtils;
import org.laokou.common.domain.annotation.CommandLog;
import org.laokou.common.excel.util.ExcelUtils;
import org.springframework.stereotype.Component;
import static org.laokou.common.tenant.constant.DSConstants.DOMAIN;

/**
 * 导出通知日志命令执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class NoticeLogExportCmdExe {

	private final NoticeLogMapper noticeLogMapper;

	@CommandLog
	public void executeVoid(NoticeLogExportCmd cmd) {
		// 校验参数
		try {
			DynamicDataSourceContextHolder.push(DOMAIN);
			ExcelUtils.doExport("通知日志", "通知日志", ResponseUtils.getHttpServletResponse(), cmd, noticeLogMapper,
					NoticeLogExcel.class, NoticeLogConvertor.INSTANCE);
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

}
