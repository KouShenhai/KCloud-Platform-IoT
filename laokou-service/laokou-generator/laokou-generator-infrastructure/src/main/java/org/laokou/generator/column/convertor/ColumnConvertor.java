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

package org.laokou.generator.column.convertor;

import org.laokou.generator.column.gatewayimpl.database.dataobject.ColumnDO;
import org.laokou.generator.column.dto.clientobject.ColumnCO;
import org.laokou.generator.column.model.ColumnE;

/**
 *
 * 代码生成器字段转换器.
 *
 * @author laokou
 */
public class ColumnConvertor {

	public static ColumnDO toDataObject(ColumnE columnE, boolean isInsert) {
		ColumnDO columnDO = new ColumnDO();
		if (isInsert) {
			columnDO.generatorId();
		}
		else {
			columnDO.setId(columnE.getId());
		}
		columnDO.setName(columnE.getName());
		columnDO.setComment(columnE.getComment());
		columnDO.setType(columnE.getType());
		columnDO.setRequiredFlag(columnE.getRequiredFlag());
		columnDO.setSaveFlag(columnE.getSaveFlag());
		columnDO.setModifyFlag(columnE.getModifyFlag());
		columnDO.setQueryFlag(columnE.getQueryFlag());
		columnDO.setPageFlag(columnE.getPageFlag());
		columnDO.setQueryType(columnE.getQueryType());
		columnDO.setComponentType(columnE.getComponentType());
		columnDO.setDictType(columnE.getDictType());
		columnDO.setFieldName(columnE.getFieldName());
		columnDO.setFieldType(columnE.getFieldType());
		columnDO.setInfoId(columnE.getInfoId());
		return columnDO;
	}

	public static ColumnCO toClientObject(ColumnDO columnDO) {
		ColumnCO columnCO = new ColumnCO();
		columnCO.setName(columnDO.getName());
		columnCO.setComment(columnDO.getComment());
		columnCO.setType(columnDO.getType());
		columnCO.setRequiredFlag(columnDO.getRequiredFlag());
		columnCO.setSaveFlag(columnDO.getSaveFlag());
		columnCO.setModifyFlag(columnDO.getModifyFlag());
		columnCO.setQueryFlag(columnDO.getQueryFlag());
		columnCO.setPageFlag(columnDO.getPageFlag());
		columnCO.setQueryType(columnDO.getQueryType());
		columnCO.setComponentType(columnDO.getComponentType());
		columnCO.setDictType(columnDO.getDictType());
		columnCO.setFieldName(columnDO.getFieldName());
		columnCO.setFieldType(columnDO.getFieldType());
		columnCO.setInfoId(columnDO.getInfoId());
		return columnCO;
	}

	public static ColumnE toEntity(ColumnCO columnCO) {
		ColumnE columnE = new ColumnE();
		columnE.setName(columnCO.getName());
		columnE.setComment(columnCO.getComment());
		columnE.setType(columnCO.getType());
		columnE.setRequiredFlag(columnCO.getRequiredFlag());
		columnE.setSaveFlag(columnCO.getSaveFlag());
		columnE.setModifyFlag(columnCO.getModifyFlag());
		columnE.setQueryFlag(columnCO.getQueryFlag());
		columnE.setPageFlag(columnCO.getPageFlag());
		columnE.setQueryType(columnCO.getQueryType());
		columnE.setComponentType(columnCO.getComponentType());
		columnE.setDictType(columnCO.getDictType());
		columnE.setFieldName(columnCO.getFieldName());
		columnE.setFieldType(columnCO.getFieldType());
		columnE.setInfoId(columnCO.getInfoId());
		return columnE;
	}

}
