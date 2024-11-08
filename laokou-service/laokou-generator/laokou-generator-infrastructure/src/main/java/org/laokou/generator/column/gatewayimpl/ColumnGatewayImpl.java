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

package org.laokou.generator.column.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.generator.column.model.ColumnE;
import org.springframework.stereotype.Component;
import org.laokou.generator.column.gateway.ColumnGateway;
import org.laokou.generator.column.gatewayimpl.database.ColumnMapper;
import java.util.Arrays;
import org.laokou.generator.column.convertor.ColumnConvertor;
import org.laokou.generator.column.gatewayimpl.database.dataobject.ColumnDO;

/**
 *
 * 代码生成器字段网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ColumnGatewayImpl implements ColumnGateway {

	private final ColumnMapper columnMapper;

	@Override
	public void create(ColumnE columnE) {
		columnMapper.insert(ColumnConvertor.toDataObject(columnE, true));
	}

	@Override
	public void update(ColumnE columnE) {
		ColumnDO columnDO = ColumnConvertor.toDataObject(columnE, false);
		columnDO.setVersion(columnMapper.selectVersion(columnE.getId()));
		columnMapper.updateById(columnDO);
	}

	@Override
	public void delete(Long[] ids) {
		columnMapper.deleteByIds(Arrays.asList(ids));
	}

}
