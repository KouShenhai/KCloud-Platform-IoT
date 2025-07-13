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

package org.laokou.admin.oss.convertor;

import org.laokou.admin.oss.factory.OssDomainFactory;
import org.laokou.admin.oss.gatewayimpl.database.dataobject.OssDO;
import org.laokou.common.core.util.ConvertUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.admin.oss.dto.clientobject.OssCO;
import org.laokou.admin.oss.model.OssE;
import java.util.List;

/**
 * OSS转换器.
 *
 * @author laokou
 */
public class OssConvertor {

	public static OssDO toDataObject(Long id, OssE ossE) {
		OssDO ossDO = ConvertUtils.sourceToTarget(ossE, OssDO.class);
		if (ObjectUtils.isNull(ossDO.getId())) {
			ossDO.setId(id);
		}
		return ossDO;
	}

	public static OssCO toClientObject(OssDO ossDO) {
		OssCO ossCO = new OssCO();
		ossCO.setId(ossDO.getId());
		ossCO.setName(ossDO.getName());
		ossCO.setType(ossDO.getType());
		ossCO.setParam(ossDO.getParam());
		ossCO.setStatus(ossDO.getStatus());
		ossCO.setCreateTime(ossDO.getCreateTime());
		return ossCO;
	}

	public static List<OssCO> toClientObjects(List<OssDO> list) {
		return list.stream().map(OssConvertor::toClientObject).toList();
	}

	public static OssE toEntity(OssCO ossCO) {
		OssE ossE = OssDomainFactory.getOss();
		ossE.setId(ossCO.getId());
		ossE.setName(ossCO.getName());
		ossE.setType(ossCO.getType());
		ossE.setParam(ossCO.getParam());
		ossE.setStatus(ossCO.getStatus());
		return ossE;
	}

}
