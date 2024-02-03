/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.i18n.common.DatasourceDataTypeEnums;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.generator.domain.Table;
import org.laokou.generator.domain.TableColumn;
import org.laokou.generator.repository.TableColumnDO;
import org.laokou.generator.repository.TableDO;
import org.laokou.generator.repository.TableMapper;
import org.laokou.generator.service.TableService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.laokou.common.i18n.common.StringConstants.UNDER;

/**
 * @author laokou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {

	private final TableMapper tableMapper;

	private static final String PRIMARY_KEY = "PRI";

	@Override
	public List<Table> findList(Set<String> tableNames) {
		Map<String, String> tables = tableMapper.selectTables(tableNames);
		List<TableColumnDO> tableColumns = tableMapper.selectTableColumns(tableNames);
		Map<String, List<TableColumnDO>> maps = tableColumns.stream()
			.collect(Collectors.groupingBy(TableColumnDO::getTableName));
		return convert(tables, maps);
	}

	private List<Table> convert(Map<String, String> tables, Map<String, List<TableColumnDO>> maps) {
		maps.forEach((key, value) -> {

		});
		return null;
	}

	private TableColumnDO getPkColumn(List<TableColumnDO> list) {
		List<TableColumnDO> pkList = list.stream().filter(item -> PRIMARY_KEY.equals(item.getKey())).toList();
		if (CollectionUtil.isNotEmpty(pkList)) {
			return pkList.getFirst();
		}
		return list.getFirst();
	}

	private TableColumn convert(TableColumnDO columnDO) {
		return TableColumn.builder()
			.fieldName(StringUtil.convertUnder(columnDO.getName()))
			.dataType(columnDO.getDataType())
			.comment(columnDO.getComment())
			.fieldType(DatasourceDataTypeEnums.valueOf(columnDO.getDataType().toUpperCase()).getValue())
			.build();
	}

	private Table convert(TableDO tableDO, List<TableColumn> columns, TableColumn pkColumn) {
		return Table.builder()
			.name(tableDO.getName())
			.comment(tableDO.getComment())
			.fields(columns)
			.pkField(pkColumn)
			.className(StringUtil.convertUnder(UNDER.concat(tableDO.getName())))
			.instanceName(StringUtil.convertUnder(tableDO.getName()))
			.build();
	}

}
