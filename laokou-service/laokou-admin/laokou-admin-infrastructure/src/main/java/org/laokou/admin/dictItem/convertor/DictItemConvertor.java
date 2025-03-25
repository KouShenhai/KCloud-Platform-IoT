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

package org.laokou.admin.dictItem.convertor;

import org.laokou.admin.dictItem.dto.clientobject.DictItemCO;
import org.laokou.admin.dictItem.gatewayimpl.database.dataobject.DictItemDO;
import org.laokou.admin.dictItem.model.DictItemE;
import org.laokou.common.core.util.ConvertUtils;
import org.laokou.common.core.util.IdGenerator;
import org.laokou.common.i18n.util.ObjectUtils;

/**
 * 字典项转换器.
 *
 * @author laokou
 */
public class DictItemConvertor {

	public static DictItemDO toDataObject(DictItemE dictItemE) {
		DictItemDO dictItemDO = ConvertUtils.sourceToTarget(dictItemE, DictItemDO.class);
		if (ObjectUtils.isNull(dictItemDO.getId())) {
			dictItemDO.setId(IdGenerator.defaultSnowflakeId());
		}
		return dictItemDO;
	}

	public static DictItemCO toClientObject(DictItemDO dictItemDO) {
		return ConvertUtils.sourceToTarget(dictItemDO, DictItemCO.class);
	}

	public static DictItemE toEntity(DictItemCO dictItemCO) {
		return ConvertUtils.sourceToTarget(dictItemCO, DictItemE.class);
	}

}
