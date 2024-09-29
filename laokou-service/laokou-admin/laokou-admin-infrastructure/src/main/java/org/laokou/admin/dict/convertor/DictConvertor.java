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

package org.laokou.admin.dict.convertor;

import org.laokou.admin.dict.dto.clientobject.DictCO;
import org.laokou.admin.dict.gatewayimpl.database.dataobject.DictDO;
import org.laokou.admin.dict.model.DictE;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.utils.ObjectUtil;

/**
 * 字典转换器.
 *
 * @author laokou
 */
public class DictConvertor {

	public static DictDO toDataObject(DictE dictE) {
		DictDO dictDO = ConvertUtil.sourceToTarget(dictE, DictDO.class);
		if (ObjectUtil.isNull(dictDO.getId())) {
			dictDO.generatorId();
		}
		return dictDO;
	}

	public static DictCO toClientObject(DictDO dictDO) {
		return ConvertUtil.sourceToTarget(dictDO, DictCO.class);
	}

	public static DictE toEntity(DictCO dictCO) {
		return ConvertUtil.sourceToTarget(dictCO, DictE.class);
	}

}
