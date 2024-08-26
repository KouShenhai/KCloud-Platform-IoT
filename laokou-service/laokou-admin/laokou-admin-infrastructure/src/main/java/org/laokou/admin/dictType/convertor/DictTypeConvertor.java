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

package org.laokou.admin.dictType.convertor;

import org.laokou.admin.dictType.dto.clientobject.DictTypeCO;
import org.laokou.admin.dictType.gatewayimpl.database.dataobject.DictTypeDO;
import org.laokou.admin.dictType.model.DictTypeE;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.utils.ObjectUtil;

/**
 * 字典类型转换器.
 *
 * @author laokou
 */
public class DictTypeConvertor {

	public static DictTypeDO toDataObject(DictTypeE dictTypeE) {
		DictTypeDO dictTypeDO = ConvertUtil.sourceToTarget(dictTypeE, DictTypeDO.class);
		if (ObjectUtil.isNull(dictTypeDO.getId())) {
			dictTypeDO.generatorId();
		}
		return dictTypeDO;
	}

	public static DictTypeCO toClientObject(DictTypeDO dictTypeDO) {
		return ConvertUtil.sourceToTarget(dictTypeDO, DictTypeCO.class);
	}

	public static DictTypeE toEntity(DictTypeCO dictTypeCO) {
		return ConvertUtil.sourceToTarget(dictTypeCO, DictTypeE.class);
	}

}
