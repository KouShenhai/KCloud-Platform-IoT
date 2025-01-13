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

package org.laokou.generator.info.convertor;

import org.laokou.generator.info.gatewayimpl.database.dataobject.InfoDO;
import org.laokou.generator.info.dto.clientobject.InfoCO;
import org.laokou.generator.info.model.InfoE;

/**
 *
 * 代码生成器信息转换器.
 *
 * @author laokou
 */
public class InfoConvertor {

	public static InfoDO toDataObject(InfoE infoE, boolean isInsert) {
		InfoDO infoDO = new InfoDO();
		if (isInsert) {
			infoDO.generatorId();
		}
		else {
			infoDO.setId(infoE.getId());
		}
		infoDO.setDatabaseName(infoE.getDatabaseName());
		infoDO.setTableName(infoE.getTableName());
		infoDO.setAuthor(infoE.getAuthor());
		infoDO.setComment(infoE.getComment());
		infoDO.setPackageName(infoE.getPackageName());
		infoDO.setPath(infoE.getPath());
		infoDO.setVersionNumber(infoE.getVersionNumber());
		infoDO.setTablePrefix(infoE.getTablePrefix());
		infoDO.setModuleName(infoE.getModuleName());
		infoDO.setAppId(infoE.getAppId());
		infoDO.setSourceId(infoE.getSourceId());
		return infoDO;
	}

	public static InfoCO toClientObject(InfoDO infoDO) {
		InfoCO infoCO = new InfoCO();
		infoCO.setDatabaseName(infoDO.getDatabaseName());
		infoCO.setTableName(infoDO.getTableName());
		infoCO.setAuthor(infoDO.getAuthor());
		infoCO.setComment(infoDO.getComment());
		infoCO.setPackageName(infoDO.getPackageName());
		infoCO.setPath(infoDO.getPath());
		infoCO.setVersionNumber(infoDO.getVersionNumber());
		infoCO.setTablePrefix(infoDO.getTablePrefix());
		infoCO.setModuleName(infoDO.getModuleName());
		infoCO.setAppId(infoDO.getAppId());
		infoCO.setSourceId(infoDO.getSourceId());
		return infoCO;
	}

	public static InfoE toEntity(InfoCO infoCO) {
		InfoE infoE = new InfoE();
		infoE.setDatabaseName(infoCO.getDatabaseName());
		infoE.setTableName(infoCO.getTableName());
		infoE.setAuthor(infoCO.getAuthor());
		infoE.setComment(infoCO.getComment());
		infoE.setPackageName(infoCO.getPackageName());
		infoE.setPath(infoCO.getPath());
		infoE.setVersionNumber(infoCO.getVersionNumber());
		infoE.setTablePrefix(infoCO.getTablePrefix());
		infoE.setModuleName(infoCO.getModuleName());
		infoE.setAppId(infoCO.getAppId());
		infoE.setSourceId(infoCO.getSourceId());
		return infoE;
	}

}
