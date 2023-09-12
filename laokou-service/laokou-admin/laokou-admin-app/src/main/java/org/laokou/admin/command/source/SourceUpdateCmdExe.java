/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

package org.laokou.admin.command.source;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.SourceConvertor;
import org.laokou.admin.domain.gateway.SourceGateway;
import org.laokou.admin.domain.source.Source;
import org.laokou.admin.dto.source.SourceUpdateCmd;
import org.laokou.admin.gatewayimpl.database.SourceMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.SourceDO;
import org.laokou.common.core.utils.RegexUtil;
import org.laokou.common.i18n.common.GlobalException;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import static org.laokou.admin.common.BizCode.ID_NOT_NULL;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class SourceUpdateCmdExe {

	private final SourceGateway sourceGateway;

	private final SourceMapper sourceMapper;

	public Result<Boolean> execute(SourceUpdateCmd cmd) {
		Source source = SourceConvertor.toEntity(cmd.getSourceCO());
		validate(source);
		return Result.of(sourceGateway.update(source));
	}

	private void validate(Source source) {
		Long id = source.getId();
		String name = source.getName();
		if (id == null) {
			throw new GlobalException(ID_NOT_NULL);
		}
		boolean sourceRegex = RegexUtil.sourceRegex(name);
		if (!sourceRegex) {
			throw new GlobalException("数据源名称必须包含字母、下划线和数字");
		}
		Long count = sourceMapper.selectCount(Wrappers.query(SourceDO.class).eq("name", name).ne("id", source.getId()));
		if (count > 0) {
			throw new GlobalException("数据源名称已存在，请重新填写");
		}
	}

}
