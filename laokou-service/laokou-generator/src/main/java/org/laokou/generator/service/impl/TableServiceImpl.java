/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.generator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.common.DatasourceDataTypeEnum;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.generator.domain.Table;
import org.laokou.generator.domain.TableColumn;
import org.laokou.generator.dto.GenerateCmd;
import org.laokou.generator.repository.TableColumnDO;
import org.laokou.generator.repository.TableDO;
import org.laokou.generator.repository.TableMapper;
import org.laokou.generator.service.TableService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.laokou.common.i18n.common.constants.StringConstant.EMPTY;
import static org.laokou.common.i18n.common.constants.StringConstant.UNDER;

/**
 * @author laokou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {

	private final TableMapper tableMapper;

	@Override
	public List<Table> findList(GenerateCmd cmd) {
		List<TableDO> tables = tableMapper.selectTables(cmd.getTables());
		List<TableColumnDO> tableColumns = tableMapper.selectTableColumns(cmd.getTables());
		Map<String, List<TableColumnDO>> map = tableColumns.stream()
			.collect(Collectors.groupingBy(TableColumnDO::getTableName));
		Map<String, String> tableMap = tables.stream().collect(Collectors.toMap(TableDO::getName, TableDO::getComment));
		return convert(cmd, tableMap, map);
	}

	private List<Table> convert(GenerateCmd cmd, Map<String, String> tableMap, Map<String, List<TableColumnDO>> map) {
		List<Table> tableList = new ArrayList<>(map.size());
		map.forEach((tableName, items) -> {
			String tableComment = tableMap.get(tableName);
			List<TableColumn> columns = items.stream().map(this::convert).toList();
			tableList.add(convert(tableName, tableComment, cmd.getTablePrefix(), columns));
		});
		return tableList;
	}

	private TableColumn convert(TableColumnDO columnDO) {
		return TableColumn.builder()
			.name(columnDO.getName())
			.fieldName(StringUtil.convertUnder(columnDO.getName()))
			.dataType(columnDO.getDataType())
			.comment(columnDO.getComment())
			.fieldType(DatasourceDataTypeEnum.valueOf(columnDO.getDataType().toUpperCase()).getValue())
			.build();
	}

	private Table convert(String name, String comment, String tablePrefix, List<TableColumn> columns) {
		String newName = name.replace(tablePrefix, EMPTY);
		return Table.builder()
			.name(name)
			.comment(comment)
			.fields(columns)
			.className(StringUtil.convertUnder(UNDER.concat(newName)))
			.instanceName(StringUtil.convertUnder(newName))
			.build();
	}

}
