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

package org.laokou.admin.command.dict.query;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.dto.common.clientobject.OptionCO;
import org.laokou.admin.dto.dict.DictOptionListQry;
import org.laokou.admin.gatewayimpl.database.DictMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.DictDO;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DictOptionListQryExe {

	private final DictMapper dictMapper;

	public Result<List<OptionCO>> execute(DictOptionListQry qry) {
		List<DictDO> list = dictMapper.selectList(Wrappers.query(DictDO.class)
			.eq("type", qry.getType())
			.select("label", "value")
			.orderByDesc("create_date"));
		if (CollectionUtil.isEmpty(list)) {
			return Result.of(new ArrayList<>(0));
		}
		return Result.of(ConvertUtil.sourceToTarget(list, OptionCO.class));
	}

}
