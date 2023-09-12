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

package org.laokou.admin.command.source.query;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.dto.common.clientobject.OptionCO;
import org.laokou.admin.dto.source.SourceOptionListQry;
import org.laokou.admin.gatewayimpl.database.SourceMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.SourceDO;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class SourceOptionListQryExe {

	private final SourceMapper sourceMapper;

	public Result<List<OptionCO>> execute(SourceOptionListQry qry) {
		List<SourceDO> list = sourceMapper.selectList(Wrappers.query(SourceDO.class).select("id", "name"));
		if (CollectionUtil.isEmpty(list)) {
			return Result.of(new ArrayList<>(0));
		}
		List<OptionCO> options = new ArrayList<>(list.size());
		for (SourceDO sourceDO : list) {
			OptionCO optionCO = new OptionCO();
			optionCO.setValue(String.valueOf(sourceDO.getId()));
			optionCO.setLabel(sourceDO.getName());
			options.add(optionCO);
		}
		return Result.of(options);
	}

}
