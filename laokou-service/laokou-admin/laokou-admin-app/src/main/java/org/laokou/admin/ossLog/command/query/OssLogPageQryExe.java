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

package org.laokou.admin.ossLog.command.query;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.ossLog.convertor.OssLogConvertor;
import org.laokou.admin.ossLog.dto.OssLogPageQry;
import org.laokou.admin.ossLog.dto.clientobject.OssLogCO;
import org.laokou.admin.ossLog.gatewayimpl.database.OssLogMapper;
import org.laokou.admin.ossLog.gatewayimpl.database.dataobject.OssLogDO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.common.tenant.constant.DSConstants.DOMAIN;

/**
 * 分页查询OSS日志请求执行器.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OssLogPageQryExe {

	private final OssLogMapper ossLogMapper;

	public Result<Page<OssLogCO>> execute(OssLogPageQry qry) {
		try {
			DynamicDataSourceContextHolder.push(DOMAIN);
			List<OssLogDO> list = ossLogMapper.selectObjectPage(qry);
			long total = ossLogMapper.selectObjectCount(qry);
			return Result.ok(Page.create(list.stream().map(OssLogConvertor::toClientObject).toList(), total));
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

}
