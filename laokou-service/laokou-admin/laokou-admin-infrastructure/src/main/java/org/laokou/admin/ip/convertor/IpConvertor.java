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

package org.laokou.admin.ip.convertor;

import org.laokou.admin.ip.dto.clientobject.IpCO;
import org.laokou.admin.ip.gatewayimpl.database.dataobject.IpDO;
import org.laokou.admin.ip.model.IpE;
import org.laokou.common.core.util.ConvertUtils;
import org.laokou.common.core.util.IdGenerator;
import org.laokou.common.i18n.util.ObjectUtils;

/**
 * IP转换器.
 *
 * @author laokou
 */
public class IpConvertor {

	public static IpDO toDataObject(IpE ipE) {
		IpDO ipDO = ConvertUtils.sourceToTarget(ipE, IpDO.class);
		if (ObjectUtils.isNull(ipDO.getId())) {
			ipDO.setId(IdGenerator.defaultSnowflakeId());
		}
		return ipDO;
	}

	public static IpCO toClientObject(IpDO ipDO) {
		return ConvertUtils.sourceToTarget(ipDO, IpCO.class);
	}

	public static IpE toEntity(IpCO ipCO) {
		return ConvertUtils.sourceToTarget(ipCO, IpE.class);
	}

}
