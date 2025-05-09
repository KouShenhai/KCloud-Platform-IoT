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

package org.laokou.admin.i18nMessage.convertor;

import org.laokou.admin.i18nMessage.dto.clientobject.I18nMessageCO;
import org.laokou.admin.i18nMessage.gatewayimpl.database.dataobject.I18nMessageDO;
import org.laokou.admin.i18nMessage.model.I18nMessageE;
import org.laokou.common.core.util.ConvertUtils;
import org.laokou.common.i18n.util.ObjectUtils;

/**
 * 国际化消息转换器.
 *
 * @author laokou
 */
public class I18nMessageConvertor {

	public static I18nMessageDO toDataObject(Long id, I18nMessageE i18nMessageE) {
		I18nMessageDO i18nMessageDO = ConvertUtils.sourceToTarget(i18nMessageE, I18nMessageDO.class);
		if (ObjectUtils.isNull(i18nMessageDO.getId())) {
			i18nMessageDO.setId(id);
		}
		return i18nMessageDO;
	}

	public static I18nMessageCO toClientObject(I18nMessageDO i18nMessageDO) {
		return ConvertUtils.sourceToTarget(i18nMessageDO, I18nMessageCO.class);
	}

	public static I18nMessageE toEntity(I18nMessageCO i18nMessageCO) {
		return ConvertUtils.sourceToTarget(i18nMessageCO, I18nMessageE.class);
	}

}
