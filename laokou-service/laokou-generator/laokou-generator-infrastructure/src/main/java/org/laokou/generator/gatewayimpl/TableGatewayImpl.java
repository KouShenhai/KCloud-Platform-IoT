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

package org.laokou.generator.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.generator.gateway.TableGateway;
import org.laokou.generator.gatewayimpl.database.TableColumnMapper;
import org.laokou.generator.gatewayimpl.database.TableMapper;
import org.laokou.generator.gatewayimpl.database.dataobject.TableColumnDO;
import org.laokou.generator.gatewayimpl.database.dataobject.TableDO;
import org.laokou.generator.model.DataType;
import org.laokou.generator.model.TableColumnV;
import org.laokou.generator.model.TableE;
import org.laokou.generator.model.TableV;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;
import static org.laokou.common.i18n.common.constant.StringConstant.UNDER;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class TableGatewayImpl implements TableGateway {

	private final TableMapper tableMapper;

	private final TableColumnMapper tableColumnMapper;

	public List<TableV> list(TableE tableE) {
		List<TableDO> tables = tableMapper.selectObjects(tableE.getTable());
		List<TableColumnDO> tableColumns = tableColumnMapper.selectObjects(tableE.getTable());
		Map<String, List<TableColumnDO>> cloumnMap = tableColumns.stream()
			.collect(Collectors.groupingBy(TableColumnDO::getTableName));
		Map<String, String> tableMap = tables.stream().collect(Collectors.toMap(TableDO::getName, TableDO::getComment));
		return convert(tableE, tableMap, cloumnMap);
	}

	private List<TableV> convert(TableE tableE, Map<String, String> tableMap,
			Map<String, List<TableColumnDO>> cloumnMap) {
		List<TableV> tableVList = new ArrayList<>(cloumnMap.size());
		cloumnMap.forEach((tableName, items) -> {
			String tableComment = tableMap.get(tableName);
			List<TableColumnV> columns = items.stream().map(this::convert).toList();
			tableVList.add(convert(tableName, tableComment, tableE.getTablePrefix(), columns));
		});
		return tableVList;
	}

	private TableColumnV convert(TableColumnDO columnDO) {
		String fieldName = StringUtil.convertUnder(columnDO.getName());
		String fieldType = DataType.valueOf(columnDO.getDataType().toUpperCase()).getValue();
		return new TableColumnV(columnDO.getComment(), fieldName, fieldType);
	}

	private TableV convert(String name, String comment, String tablePrefix, List<TableColumnV> columns) {
		String newName = name.replace(tablePrefix, EMPTY);
		String className = StringUtil.convertUnder(UNDER.concat(newName));
		String instanceName = StringUtil.convertUnder(newName);
		return new TableV(name, comment, columns, className, instanceName);
	}

}
